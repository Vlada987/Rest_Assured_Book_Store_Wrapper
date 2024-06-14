package tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import pojo.Payload;
import pojo.Payload2;
import pojo.Payload3;
import rest.Context;
import rest.EContentType;
import rest.ERestMethod;
import rest.Methods;

public class TestClass {

	Random random = new Random();
	String booksBaseURL = "https://simple-books-api.glitch.me";
	String token = "";
	String orderId;

//@Test(priority=1)
	public void Test_01_Get_Status() {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/status";
		context.eRestMethod = ERestMethod.GET;
		Response res = Methods.GET(context);

		String resContType = res.contentType();
		String status = res.jsonPath().get("status");
		int statusCode = res.getStatusCode();

		Assert.assertEquals(status, "OK");
		Assert.assertEquals(statusCode, 200);
		Assert.assertTrue(resContType.contains("application/json"));
	}

//@Test(priority=2)
	public void Test_02_Get_ListOfBooks() {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/books";
		context.eRestMethod = ERestMethod.GET;
		Response res = Methods.GET(context);

		String resContType = res.contentType();
		String body = res.body().toString();
		int statusCode = res.getStatusCode();
		int size = Methods.getArraySize(res, "id");

		Assert.assertEquals(statusCode, 200);
		Assert.assertTrue(resContType.contains("application/json"));
		Assert.assertTrue(!body.isEmpty());
		Assert.assertEquals(size, 6);
	}

//@Test(priority=3)
	public void Test_03_Get_BookByID() {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/books/{bookId}";
		context.pathParams.put("bookId", 2);
		context.eRestMethod = ERestMethod.GET;
		Response res = Methods.GET(context);

		String resContType = res.contentType();
		String author = res.jsonPath().get("author");
		int statusCode = res.getStatusCode();

		Assert.assertEquals(statusCode, 200);
		Assert.assertTrue(author.equals("Cicely Tyson"));
		Assert.assertTrue(resContType.contains("application/json"));
	}

	@Test(priority = 4)
	public void Test_04_Authenticate() throws JSONException {

		Context context = new Context();
		Payload user = new Payload();
		user.setClientName("pera");
		user.setClientEmail(Methods.createMail());
		context.baseURL = booksBaseURL;
		context.requestContentType = EContentType.JSON;
		context.URI = "/api-clients/";
		context.requestBody = new JSONObject(user);
		context.eRestMethod = ERestMethod.POST;
		Response res = Methods.POST(context);

		token = res.jsonPath().get("accessToken");
		String resContType = res.contentType();
		int statusCode = res.getStatusCode();

		Assert.assertEquals(statusCode, 201);
		Assert.assertTrue(token.length() > 25);
		Assert.assertTrue(resContType.contains("application/json"));
	}

	@Test(priority = 5)
	public void Test_05_Order_A_Book() throws JSONException {

		Context context = new Context();
		Payload2 order = new Payload2();
		order.setBookId(1);
		order.setCustomerName("mutimir");
		context.baseURL = booksBaseURL;
		context.requestContentType = EContentType.JSON;
		context.URI = "/orders";
		Map<String, Object> header = new HashMap<>();
		header.put("Authorization", "Bearer " + token);
		context.requestHeaderParams = header;
		context.requestBody = new JSONObject(order);
		context.eRestMethod = ERestMethod.POST;
		Response res = Methods.POST(context);

		orderId = res.jsonPath().get("orderId");
		Assert.assertTrue(res.getStatusCode() == 201);
		Assert.assertTrue(res.jsonPath().get("created").equals(true));
	}

	@Test(priority = 6)
	public void Test_06_GetOrder_ByID() {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/orders/{orderId}";
		Map<String, Object> header = new HashMap<>();
		header.put("Authorization", "Bearer " + token);
		context.requestHeaderParams = header;
		context.pathParams.put("orderId", orderId);
		context.eRestMethod = ERestMethod.GET;
		Response res = Methods.GET(context);

		String name = res.jsonPath().get("customerName");
		Assert.assertTrue(res.statusCode() == 200);
		Assert.assertTrue(name.equals("mutimir"));
	}

	@Test(priority = 7)
	public void Test_07_UpdateOrder() throws JSONException {

		Context context = new Context();
		Payload3 newName = new Payload3();
		newName.setCustomerName("noah");
		context.baseURL = booksBaseURL;
		context.URI = "/orders/{orderId}";
		Map<String, Object> header = new HashMap<>();
		header.put("Authorization", "Bearer " + token);
		context.requestHeaderParams = header;
		context.requestContentType = EContentType.JSON;
		context.pathParams.put("orderId", orderId);
		context.requestBody = new JSONObject(newName);
		Response res = Methods.PATCH(context);
		res.then().log().all();
		Assert.assertTrue(res.statusCode() == 204);
		Assert.assertTrue(res.time() < 2500);

	}

	@Test(priority = 8)
	public void Test_08_GetOrder_ByID_AfterUpdate() {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/orders/{orderId}";
		Map<String, Object> header = new HashMap<>();
		header.put("Authorization", "Bearer " + token);
		context.requestHeaderParams = header;
		context.pathParams.put("orderId", orderId);
		context.eRestMethod = ERestMethod.GET;
		Response res = Methods.GET(context);

		String name = res.jsonPath().get("customerName");
		Assert.assertTrue(res.statusCode() == 200);
		Assert.assertTrue(name.equals("noah"));
	}

	@Test(priority = 9)
	public void Test_09_DeleteOrder() throws JSONException {

		Context context = new Context();
		context.baseURL = booksBaseURL;
		context.URI = "/orders/{orderId}";
		Map<String, Object> header = new HashMap<>();
		header.put("Authorization", "Bearer " + token);
		context.requestHeaderParams = header;
		context.pathParams.put("orderId", orderId);
		context.eRestMethod = ERestMethod.DELETE;
		Response res = Methods.DELETE(context);

		Assert.assertTrue(res.statusCode() == 204);
	}

}
