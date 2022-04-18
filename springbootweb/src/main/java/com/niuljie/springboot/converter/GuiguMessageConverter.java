package com.niuljie.springboot.converter;

import com.niuljie.springboot.dto.Person;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 自定义的converter--只关心写操作，读忽略
 */
public class GuiguMessageConverter implements HttpMessageConverter<Person> {
    /**
     * Indicates whether the given class can be read by this converter.
     */
    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    /**
     * Indicates whether the given class can be written by this converter.
     */
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        //判断参数类型是否是T
        return clazz.isAssignableFrom(Person.class);
    }

    /**
     * 服务器要统计所有MessageConverter都能写出哪些内容类型
     * application/x-guigu
     */
    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return MediaType.parseMediaTypes("application/x-guigu");
    }

    /**
     * Read an object of the given type from the given input message, and returns it.
     */
    @Override
    public Person read(Class<? extends Person> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    /**
     * Write an given object to the given output message.
     */
    @Override
    public void write(Person person, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        //自定义协议数据的写出
        String data = person.getUserName() + ";" + person.getAge() + ";" + person.getBirth();
        //写出去
        OutputStream body = outputMessage.getBody();
        //浏览器的编码是GBK
        body.write(data.getBytes("GBK"));
    }
}
