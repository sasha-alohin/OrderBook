package com.softbistro.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.softbistro.order.Book;
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

	public List<Book> getCatalog() {
		String jsonText;
		JSONObject jsonBook;
		JSONArray jsonArrayResult;
		List<Book> books = new ArrayList<>();
		List<Object> authorList = new ArrayList<>();
		JSONArray authorJsonArray;
		jsonText = readAll(URL_GET_CATALOG);
		jsonArrayResult = new JSONArray(new JSONObject(jsonText).get("result").toString());
		JSONArray booksJson = new JSONArray(new JSONObject(
				new JSONObject(new JSONObject(jsonArrayResult.get(0).toString()).get("tbs-book").toString())
						.get("responseContent").toString()).get("docs").toString());
		for (Object object : booksJson) {
			jsonBook = new JSONObject(object.toString());
			authorJsonArray = new JSONArray(jsonBook.get("authors").toString());
			authorJsonArray.forEach(author -> authorList.add(author));
			books.add(new Book(jsonBook.getString("id"), jsonBook.getString("title"), jsonBook.getString("isbn"),
					jsonBook.getString("ean"), jsonBook.getString("imageUri"), authorList));
		}
		return books;
	}

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
}
