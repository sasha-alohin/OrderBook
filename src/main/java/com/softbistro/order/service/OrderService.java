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
	private String URL_GET_PRICES = "";
	@Value("${orders.url-for-create-order}")
	private String URL_CREATE_ORDER = "";
	private String jsonText;
	private final Integer userId = 317673305;
	private static CatalogItem catalogItem;
	private static Integer orderId;
	private JSONObject jsonItem;
	private JSONArray array;
	public Integer createOrder(BookForOrder book) throws JsonProcessingException {
		List<OrderItem> items = new ArrayList<>();
		items.add(new OrderItem(1, "COPS", book.getCatalogItemId(), book.getPricingId()));
		Order order = new Order(items, userId);
		orderId = new JSONObject(postToApi(order)).getInt("id");
		return orderId;
	}

	public CatalogItem getCatalogItem() {
		JSONObject jsonPrice;
		jsonText = readAll(URL_GET_PRICES);
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

	public List<Book> getCatalog() {
		List<Book> books = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		JSONArray authorJsonArray;
		jsonText = readAll(URL_GET_CATALOG);
		array = new JSONArray(new JSONObject(jsonText).get("result").toString());
		JSONArray booksJson = new JSONArray(new JSONObject(
				new JSONObject(new JSONObject(array.get(0).toString()).get("tbs-book").toString())
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

	public String postToApi(Order order) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		jsonText = mapper.writeValueAsString(order);
		Client client = Client.create();
		WebResource webResource = client.resource(URL_CREATE_ORDER);
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
