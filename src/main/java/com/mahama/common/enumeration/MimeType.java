package com.mahama.common.enumeration;

public enum MimeType {
    form("application/x-www-form-urlencoded"),
    download("application/x-download"),
    stream("application/octet-stream"),
    json("application/json"),
    xml("application/xml"),
    zip("application/x-zip-compressed"),
    doc("application/msword"),
    docx("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    xls("application/vnd.ms-excel"),
    xlsx("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    ppt("application/vnd.ms-powerpoint"),
    pptx("application/vnd.openxmlformats-officedocument.presentationml.presentation");
    private final String value;

    MimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
