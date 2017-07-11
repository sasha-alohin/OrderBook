package com.softbistro.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.messenger4j.send.buttons.Button;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.CatalogItem;
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
	private String jsonText;
	private String urlForTemplate = "http://localhost:8080/template";

	
	
	public List<CatalogItem> getCatalogItems(){
		JSONObject jsonItem;
		JSONArray jsonArrayResult;
		List<Book> books = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		JSONArray array;
		jsonText = readAll(URL_GET_CATALOG);
		jsonItem = new JSONObject(jsonText);
		System.out.println(jsonText);
		return null;
	}
	
	public void createOrder(CatalogItem item){
		
	}
	public List<Book> getCatalog() {

		JSONObject jsonBook;
		JSONArray jsonArrayResult;
		List<Book> books = new ArrayList<>();
		List<String> authorList = new ArrayList<>();
		JSONArray authorJsonArray;
		jsonText = readAll(URL_GET_CATALOG);
		jsonArrayResult = new JSONArray(new JSONObject(jsonText).get("result").toString());
		JSONArray booksJson = new JSONArray(new JSONObject(
				new JSONObject(new JSONObject(jsonArrayResult.get(0).toString()).get("tbs-book").toString())
						.get("responseContent").toString()).get("docs").toString());
		for (Object object : booksJson) {
			jsonBook = new JSONObject(object.toString());
			authorJsonArray = new JSONArray(jsonBook.get("authors").toString());
			authorJsonArray.forEach(author -> authorList.add(author.toString()));
			books.add(new Book(jsonBook.getString("id"), jsonBook.getString("title"), jsonBook.getString("isbn"),
					jsonBook.getString("ean"), jsonBook.getString("imageUri"), authorList));
		}
		return books;
	}

//	public GenericTemplate createTemplate() {
//		ListBuilder buttonBuilder;
//		jsonText = readAll(urlForTemplate);
//		System.out.println(jsonText);
//		Builder builder = GenericTemplate.newBuilder();
//		JSONArray books = new JSONArray(new JSONObject(jsonText).getJSONArray("elements").toString());
//		for (Object object : books) {
////			List<Button> buttons = new ;
//			buttonBuilder = Button.newListBuilder();
//			JSONObject json = new JSONObject(object.toString());
//			JSONArray buttonsArray = json.getJSONArray("buttons");
//			for (Object objectButton : buttonsArray) {
//				JSONObject buttonJson = new JSONObject(objectButton.toString());
//				buttons.addAll(buttonBuilder.addUrlButton(buttonJson.getString("title"), buttonJson.getString("url"))
//						.toList().build());
//			}
//			builder.addElements().addElement(json.getString("title")).subtitle(json.getString("subtitle"))
//					.itemUrl(json.getString("itemUrl")).imageUrl(json.getString("imageUrl")).buttons(buttons);
//
//		}
//		return builder.build();
//	}

	/**
	 * Read data from stream
	 * 
	 * @param rd
	 *            - reader holding stream
	 * @return data from stream
	 */
	private String readAll(String url) {
		Logger LOGGER = Logger.getLogger(OrderService.class);
		String ERROR_MESSAGE = "Can't read from site source";
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		client.addFilter(new GZIPContentEncodingFilter(false));
		String responseData = "";
		try {
			WebResource wr = client.resource(url);
			ClientResponse response = null;
			response = wr.get(ClientResponse.class);
			responseData = response.getEntity(String.class);

		} catch (Exception e) {
			LOGGER.error(ERROR_MESSAGE);
		}

		return responseData;
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
