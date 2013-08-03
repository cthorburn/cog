package com.trabajo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class BytesBufferer {

    ByteArrayOutputStream os = null;
    ByteArrayInputStream is = null;

    public BytesBufferer() {

    }

    public OutputStream getOutputStream() {
        os=new ByteArrayOutputStream();
        return os;
    }
    public InputStream getInputStream() {
        is=new ByteArrayInputStream(os.toByteArray());
        return is;
    }
}
