package rest;

public enum EContentType {

	JSON("application/json"), 
	Text("text/plain"), 
	FORM_DATA("multipart/form-data");

	String eContentType;

	EContentType(String eContentType) {
		this.eContentType = eContentType;
	}

	public String getContentType() {

		return eContentType;
	}

}
