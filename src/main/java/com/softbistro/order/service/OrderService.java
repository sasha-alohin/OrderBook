package com.softbistro.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
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
	@Value("${orders.url-for-add-item}")
	private String URL_ADD_ITEM;

	private static final Logger LOGGER = Logger.getLogger(OrderService.class);
	private String jsonText;
	private final Integer userId = 66132623;
	private static Integer orderId;
	private JSONObject jsonItem;
	private JSONArray array;

	private String checkoutFirstList = "[\n"
			+ "  { \"namespace\": \"place_order\", \"key\": \"place_order_experiment\", \"value\": \"true\" },\n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"fname\", \"value\": \"Vova\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"lname\", \"value\": \"K\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"line1\", \"value\": \"666 Dundee Road\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"line2\", \"value\": \"\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"city\", \"value\": \"Northbrook\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"state\", \"value\": \"IL\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"country\", \"value\": \"US\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"zip\", \"value\": \"60062\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"phone\", \"value\": \"5335555555\" }, \n"
			+ "    { \"namespace\": \"shipping_address\", \"key\": \"address_id\", \"value\": \"58229368\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"fname\", \"value\": \"VISA\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"lname\", \"value\": \"\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"line1\", \"value\": \"Po Box 629\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"line2\", \"value\": \"\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"city\", \"value\": \"Hartford\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"state\", \"value\": \"CT\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"country\", \"value\": \"US\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"zip\", \"value\": \"06103\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"phone\", \"value\": \"5533555555\" }, \n"
			+ "    { \"namespace\": \"billing_address\", \"key\": \"address_id\", \"value\": \"119522422\" },\n"
			+ "  { \"namespace\": \"method_of_payment1\",  \"key\": \"type\", \"value\": \"CREDITCARD\" },\n"
			+ "  { \"namespace\": \"method_of_payment1\", \"key\": \"id\", \"value\": 110556349 }, \n"
			+ "  { \"namespace\": \"method_of_payment1\", \"key\": \"amount\", \"value\": null },\n"
			+ "  {  \"namespace\": \"confirm_checkout\", \"key\": \"confirmed\", \"value\": \"1\"  }\n" + "]";
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
			+ "  { \"namespace\":\"shipping_choices1\", \"key\":\"option_chosen\", \"value\":\"";

	public void zeroCheckout(BookForOrder book) throws JsonProcessingException {
		LOGGER.info(postToApiString(checkoutZeroList, URL_ZERO_CHECKOUT + book.getOrderId() + "/CHECKOUTV3"));
	}

	public void firstCheckout(BookForOrder book) throws JsonProcessingException {
		LOGGER.info(postToApiString(checkoutFirstList, URL_FIRST_CHECKOUT + book.getOrderId() + "/CHECKOUTV3"));
	}

	public String firstEvaluateCheckout(BookForOrder book) throws JsonProcessingException {
		jsonText = postToApi(null, URL_FIRST_EVALUATE_CHECKOUT + book.getOrderId() + "/CHECKOUTV3");
		jsonItem = new JSONObject(new JSONObject(jsonText).getJSONObject("data").toString());
		return jsonItem.getString("shipping_choices1.option1-hash");
	}

	public void lastEvaluateCheckout(BookForOrder book) throws JsonProcessingException {
		LOGGER.info(postToApi(null, URL_FIRST_EVALUATE_CHECKOUT + book.getOrderId() + "/CHECKOUTV3"));
	}

	public void setShippingOptions(BookForOrder book) throws JsonProcessingException {
		LOGGER.info(postToApiString(setShippingOptions + book.getShippingChoiceHash() + "\"}\n" + "]",
				URL_FIRST_CHECKOUT + book.getOrderId() + "/CHECKOUTV3"));
	}

	public void addItem(BookForOrder book) throws JsonProcessingException {
		LOGGER.info(postToApiString(
				"[  {    \"catalogItemId\": \"" + book.getCatalogItemId() + "\",    \"pricingId\": \""
						+ book.getPricingId() + "\",    \"quantity\": " + book.getQuantity() + "  }]",
				URL_ADD_ITEM + book.getOrderId()));
	}

	public Integer createOrder(BookForOrder book) throws JsonProcessingException {
		List<OrderItem> items = new ArrayList<>();
		items.add(new OrderItem(1, "COPS", book.getPricingId(), book.getCatalogItemId()));
		Order order = new Order(items, userId);
		jsonText = postToApi(order, URL_CREATE_ORDER);
		orderId = new JSONObject(jsonText).getInt("id");
		return orderId;
	}

	public CatalogItem getCatalogItem(String catalogItemId) {
		JSONObject jsonPrice;
		jsonText = readAll(URL_GET_PRICES + catalogItemId);
		jsonItem = new JSONObject(jsonText);
		array = new JSONArray(jsonItem.get("prices").toString());
		List<PriceItem> prices = new ArrayList<>();
		for (Object priceObject : array) {
			jsonPrice = new JSONObject(priceObject.toString());
			prices.add(new PriceItem(jsonPrice.getDouble("price"), jsonPrice.getString("logId")));
		}
		return new CatalogItem(jsonItem.getString("catalogItemId"), jsonItem.getString("name"), prices);
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
					jsonItem.getString("ean"), jsonItem.getString("imgWidth80"), authorList));
		}
		return books;
	}

	public String postToApi(Object object, String url) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		jsonText = mapper.writeValueAsString(object);
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonText);

		return response.getEntity(String.class);
	}

	public String postToApiString(String body, String url) throws JsonProcessingException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, body);

		return response.getEntity(String.class);
	}

	/**
	 * Read data from stream
	 * 
	 * @param rd
	 *            - reader holding stream
	 * @return data from stream
	 */
	private String readAll(String url) {
		Client client = Client.create();
		try {
			WebResource wr = client.resource(url);
			ClientResponse response = wr.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			return response.getEntity(String.class);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
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
