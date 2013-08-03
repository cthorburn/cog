package com.trabajo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface IResources {

  Properties getAliases();

  String getPath();

  InputStream asStream(String alias);

  String asString(String alias) throws IOException;

}