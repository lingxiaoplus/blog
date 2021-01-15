package com.lingxiao.blog.global;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Jackson配置
 * @author William
 *
 */
@Configuration
public class JacksonConfig {

	/**
	 * Jackson全局转化long类型为String，解决jackson序列化时long类型缺失精度问题
	 * 实际使用中，jackson会将符合条件的string自动转换为long，所以这里加了一个这个配置
	 * @return Jackson2ObjectMapperBuilderCustomizer 注入的对象
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.serializerByType(Long.TYPE, ToStringSerializer.instance);
			jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
		};
	}
}