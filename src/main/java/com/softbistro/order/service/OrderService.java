package com.softbistro.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.BookForOrder;
import com.softbistro.order.component.CatalogItem;
import com.softbistro.order.component.Order;
import com.softbistro.order.component.OrderItem;
import com.softbistro.order.component.PriceItem;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter;

@Service
public class OrderService {
	
	@Value("${orders.url-for-catalog}")
	private String URL_GET_CATALOG;
	@Value("${orders.url-for-prices}")
	private String URL_GET_PRICES;
	@Value("${orders.url-for-create-order}")
	private String URL_CREATE_ORDER;
	@Value("${orders.url-for-first-checkout}")
	private String URL_FIRST_CHECKOUT;
	@Value("${orders.url-for-zero-checkout}")
	private String URL_ZERO_CHECKOUT;
	@Value("${orders.url-for-first-evaluate-checkout}")
	private String URL_FIRST_EVALUATE_CHECKOUT;

	private String jsonText;
	private final Integer userId = 317673305;
	private static CatalogItem catalogItem;
	private static Integer orderId;
	private JSONObject jsonItem;
	private JSONArray array;
	private static String shippingChoiceHash;
	
	
	private String checkoutFirstList = "{\"namespace\": \"billing_address\",\"key\": \"fname\",\"value\": \"John\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"lname\",\"value\": \"Douh\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"line1\",\"value\": \"1079 Seacoast Dr\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"line2\",\"value\": \"\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"city\",\"value\": \"San Diego\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"state\",\"value\": \"CA\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"country\",\"value\": \"US\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"zip\",\"value\": \"91932\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"phone\",\"value\": \"6194297507\"},\n"
			+ "  {\"namespace\": \"billing_address\",\"key\": \"address_id\",\"value\": \"1\"},\n" + "  \n"
			+ "  {\"namespace\": \"method_of_payment1\",\"key\": \"type\",\"value\": \"CREDITCARD\"},\n"
			+ "  {\"namespace\": \"method_of_payment1\",\"key\": \"id\",\"value\": 110556349, \n"
			+ "  {\"namespace\": \"method_of_payment1\",\"key\": \"amount\",\"value\": null},\n" + "  \n"
			+ "  {\"namespace\": \"confirm_checkout\",\"key\": \"confirmed\",\"value\": \"1\"}";
	private String checkoutZeroList = "[  \n" + "	\"checkout_initial\", \n" + "	\"shipping_address\", \n"
			+ "	\"calculate_item_prices_v3\",                    \n" + "	\"make_prices_consistent\",\n"
			+ "	\"tbs_verification\",                    \n" + "	\"billing_address\", \n"
			+ "	\"summate_total\", \n" + "	\"free_method_of_payment\",                    \n"
			+ "	\"method_of_payment\", \n" + "	\"subscription_verification\",                     \n"
			+ "	\"report_errors\", \n" + "	\"shipping_choices\",\n" + "	\"calculate_coupons\",                   \n"
			+ "	\"calculate_taxes\", \n" + "	\"can_fulfill\", \n" + "	\"confirm_checkout\", \n"
			+ "	\"payment_authorize_v3\",\n" + "	\"assign_order_key\",                     \n"
			+ "	\"expand_quantities\"\n" + "]";
	private String setShippingOptions = "[\n"
			+ "  { \"namespace\":\"shipping_choices1\", \"key\":\"option_chosen\", \"value\":\"{{shipping_choice_hash}}\" }\n"
			+ "]";

	public void zeroCheckout(BookForOrder book) throws JsonProcessingException {
		postToApi(checkoutZeroList, URL_ZERO_CHECKOUT + book.getOrderId() + "/CHECKOUTV3");
	}

	public void firstCheckout(BookForOrder book) throws JsonProcessingException {
		postToApi(checkoutFirstList, URL_FIRST_CHECKOUT + book.getOrderId() + "/CHECKOUTV3");
	}

	public String firstEvaluateCheckout(BookForOrder book) throws JsonProcessingException {
		jsonText = postToApi(null, URL_FIRST_EVALUATE_CHECKOUT + book.getOrderId() + "/CHECKOUTV3");
		System.out.println(jsonText);
		jsonItem = new JSONObject(
				new JSONObject(jsonText)
						.getJSONObject("data").toString());
		shippingChoiceHash = jsonItem.getString("shipping_choices1.option1-hash");
		System.out.println(shippingChoiceHash);
		return shippingChoiceHash;
	}

	public void setShippingOptions() throws JsonProcessingException {
		postToApi(setShippingOptions + shippingChoiceHash + "}\n" + 
				"]", URL_FIRST_CHECKOUT);
	}

	public Integer createOrder(BookForOrder book) throws JsonProcessingException {
		List<OrderItem> items = new ArrayList<>();
		items.add(new OrderItem(1, "COPS", book.getCatalogItemId(), book.getPricingId()));
		Order order = new Order(items, userId);
		orderId = new JSONObject(postToApi(order, URL_CREATE_ORDER)).getInt("id");
		return orderId;
	}

	public CatalogItem getCatalogItem(String catalogItemId) {
		JSONObject jsonPrice;
		jsonText = readAll(URL_GET_PRICES + catalogItemId);
		jsonItem = new JSONObject(new JSONArray(jsonText).get(0).toString());
		array = new JSONArray(jsonItem.get("prices").toString());
		List<PriceItem> prices = new ArrayList<>();
		for (Object priceObject : array) {
			jsonPrice = new JSONObject(priceObject.toString());
			prices.add(new PriceItem(jsonPrice.getDouble("price"), jsonPrice.getString("logId")));
		}
		catalogItem = new CatalogItem(jsonItem.getString("catalogItemId"), jsonItem.getString("name"), prices);
		return catalogItem;
	}

	public List<Book> getCatalog(String searchQuery) {
		List<Book> books = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		JSONArray authorJsonArray;
		jsonText = readAll(URL_GET_CATALOG + searchQuery);
		array = new JSONArray(new JSONObject(jsonText).get("result").toString());
		JSONArray booksJson = new JSONArray(
				new JSONObject(new JSONObject(new JSONObject(array.get(0).toString()).get("tbs-book").toString())
						.get("responseContent").toString()).get("docs").toString());
		for (Object object : booksJson) {
			jsonItem = new JSONObject(object.toString());
			authorJsonArray = new JSONArray(jsonItem.get("authors").toString());
			authorJsonArray.forEach(author -> authorList.add(author.toString()));
			books.add(new Book(jsonItem.getString("id"), jsonItem.getString("title"), jsonItem.getString("isbn"),
					jsonItem.getString("ean"), jsonItem.getString("imageUri"), authorList));
		}
		return books;
	}

	public String postToApi(Object object, String url) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		jsonText = mapper.writeValueAsString(object);
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, jsonText);
		String output = response.getEntity(String.class);
		return output;
	}

	/**
	 * Read data from stream
	 * 
	 * @param rd
	 *            - reader holding stream
	 * @return data from stream
	 */
	private String readAll(String url) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));
		try {
			WebResource wr = client.resource(url);
			ClientResponse response = null;
			response = wr.get(ClientResponse.class);
			jsonText = response.getEntity(String.class);

		} catch (Exception e) {
		}
		return jsonText;
	}

	public GenericTemplate createTemplate(List<Book> books) {

		final List<Button> firstLink = Button.newListBuilder()
				.addUrlButton("Biology 12th edition",
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1")
				.toList().build();
		final List<Button> secondLink = Button.newListBuilder()
				.addUrlButton("Biology 12th edition",
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1")
				.toList().build();
		final List<Button> thirdLink = Button.newListBuilder()
				.addUrlButton("Biology 12th edition",
						"http://www.chegg.com/textbooks/biology-12th-edition-9780078024269-0078024269?trackid=1f854400&strackid=4a41bf08&ii=1")
				.toList().build();

		String authors1 = books.get(0).getAuthors().get(0).toString();
		String authors2 = books.get(1).getAuthors().get(0).toString();
		String authors3 = books.get(2).getAuthors().get(0).toString();

		books.stream().limit(4).collect(Collectors.toList());

		final GenericTemplate genericTemplate = GenericTemplate.newBuilder().addElements()
				.addElement(books.get(0).getTitle()).subtitle("Authors " + authors1)
				.itemUrl("http://www.chegg.com/books").imageUrl(books.get(0).getImageUrl()).buttons(firstLink).toList()
				.addElement(books.get(1).getTitle()).subtitle("Authors " + authors2)
				.itemUrl("http://www.chegg.com/books").imageUrl(books.get(1).getImageUrl()).buttons(secondLink).toList()
				.addElement(books.get(2).getTitle()).subtitle("Authors " + authors3)
				.itemUrl("http://www.chegg.com/books").imageUrl(books.get(2).getImageUrl()).buttons(thirdLink).toList()
				.done().build();

		return genericTemplate;
	}
}
