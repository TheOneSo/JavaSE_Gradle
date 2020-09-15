package com.oneso.web.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessorImpl implements TemplateProcessor {

  private final Configuration configuration;

  public TemplateProcessorImpl(String templateDir) {
    configuration = new Configuration(Configuration.VERSION_2_3_28);
    configuration.setClassForTemplateLoading(this.getClass(), templateDir);
    configuration.setDefaultEncoding("UTF-8");
  }

  @Override
  public String getPage(String fileName, Map<String, Object> data) throws IOException {
    try(Writer stream = new StringWriter()) {
      Template template = configuration.getTemplate(fileName);
      template.process(data, stream);
      return stream.toString();
    } catch (TemplateException e) {
      throw new IOException(e);
    }
  }
}
