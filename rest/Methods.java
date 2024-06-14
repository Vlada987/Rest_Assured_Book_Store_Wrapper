package rest;

import static io.restassured.RestAssured.*;
import java.util.Map;
import java.util.Random;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Methods {

	public static void setBaseUrl(String URL) {

		Assertions.assertThat(URL).isNotEmpty();
		RestAssured.baseURI = URL;
	}

	public static void setEndpointUri(String URI) {

		Assertions.assertThat(URI).isNotEmpty();

	}

	private static RequestSpecification authorization(IAuth Icontext) {

		RequestSpecification reqSpec;
		if (Icontext == null) {
			reqSpec = RestAssured.given();
		} else {
			reqSpec = Icontext.auth();
		}
		return reqSpec;
	}

	private static RequestSpecification init(Context context) {

		Assertions.assertThat(context).isNotNull();
		setBaseUrl(context.baseURL);
		RequestSpecification reqSpec = authorization(context.auth);

		if (!context.requestHeaderParams.isEmpty()) {
			reqSpec.headers(context.responseHeaderParams);
		}
		if (!context.pathParams.isEmpty()) {
			reqSpec.pathParams(context.pathParams);
		}
		if (!context.formParams.isEmpty()) {
			reqSpec.formParams(context.formParams);
		}
		if (!context.queryParams.isEmpty()) {
			reqSpec.queryParams(context.queryParams);
		}
		if (context.requestContentType != null && !context.requestContentType.equals("")) {
			reqSpec.contentType(context.requestContentType.getContentType());
		}

		return reqSpec;
	}

	public static Response GET(Context context) {

		Response resp = null;
		RequestSpecification reqSpec = init(context);
		if (context.eRestMethod == null || context.eRestMethod.equals("")) {
			context.eRestMethod = ERestMethod.GET;
		}
		if (!context.requestHeaderParams.isEmpty()) {
			for (Map.Entry<String, Object> header : context.requestHeaderParams.entrySet()) {
				reqSpec.header(header.getKey(), header.getValue());
			}
		}
		if (context.URI != null && !context.URI.equals("")) {
			resp = reqSpec.get(context.URI);
		}
		return resp;
	}

	public static Response POST(Context context) throws JSONException {

		Response resp = null;
		RequestSpecification reqSpec = init(context);
		if (context.eRestMethod == null || context.eRestMethod.equals("")) {
			context.eRestMethod = ERestMethod.POST;
		}
		if (context.requestBody != null) {
			reqSpec.body(context.requestBody.toString());
		}
		if (!context.multiParts.isEmpty()) {
			for (Map.Entry<String, Object> multi : context.multiParts.entrySet()) {
				reqSpec.multiPart(multi.getKey(), multi.getValue());
			}
		}
		if (!context.requestHeaderParams.isEmpty()) {
			for (Map.Entry<String, Object> header : context.requestHeaderParams.entrySet()) {
				reqSpec.header(header.getKey(), header.getValue());
			}
		}

		if (context.URI != null && !context.URI.equals("")) {
			resp = reqSpec.post(context.URI);
		} else {
			resp = reqSpec.post();
		}
		return resp;
	}

	public static Response PATCH(Context context) throws JSONException {

		Response resp = null;
		RequestSpecification reqSpec = init(context);
		if (context.eRestMethod == null || context.eRestMethod.equals("")) {
			context.eRestMethod = ERestMethod.PATCH;
		}
		if (context.requestBody != null) {
			reqSpec.body(context.requestBody.toString());
		}
		if (!context.multiParts.isEmpty()) {
			for (Map.Entry<String, Object> multi : context.multiParts.entrySet()) {
				reqSpec.multiPart(multi.getKey(), multi.getValue());
			}
		}
		if (!context.requestHeaderParams.isEmpty()) {
			for (Map.Entry<String, Object> header : context.requestHeaderParams.entrySet()) {
				reqSpec.header(header.getKey(), header.getValue());
			}
		}

		if (context.URI != null && !context.URI.equals("")) {
			resp = reqSpec.patch(context.URI);
		} else {
			resp = reqSpec.patch();
		}
		return resp;
	}

	public static Response DELETE(Context context) throws JSONException {

		Response resp = null;
		RequestSpecification reqSpec = init(context);
		if (context.eRestMethod == null || context.eRestMethod.equals("")) {
			context.eRestMethod = ERestMethod.DELETE;
		}
		if (context.requestBody != null) {
			reqSpec.body(context.requestBody.toString());
		}
		if (!context.multiParts.isEmpty()) {
			for (Map.Entry<String, Object> multi : context.multiParts.entrySet()) {
				reqSpec.multiPart(multi.getKey(), multi.getValue());
			}
		}
		if (!context.requestHeaderParams.isEmpty()) {
			for (Map.Entry<String, Object> header : context.requestHeaderParams.entrySet()) {
				reqSpec.header(header.getKey(), header.getValue());
			}
		}

		if (context.URI != null && !context.URI.equals("")) {
			resp = reqSpec.delete(context.URI);
		} else {
			resp = reqSpec.delete();
		}
		return resp;
	}

	public static int getArraySize(Response res, String json) {

		int size = 0;
		while (!(res.jsonPath().get(json + "[" + size + "]") == null)) {
			size++;
		}
		return size;
	}

	public static String createMail() {

		Random random = new Random();
		String mail = "";
		String a = "per";
		String b = "@gmail.com";
		String c = String.valueOf(random.nextInt(2000));
		mail = a + c + b;

		return mail;
	}

}
