package lfs.server.common;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class JSONReader<T> {

	private Class<T> targetClass;
	private List<Class<?>> mixins = new ArrayList<>();
	
	public JSONReader(Class<T> targetClass) {
		this.targetClass = targetClass;
	}

	public void addMixIn(Class<?> mixin) {
		mixins.add(mixin);
	}
	
    public List<T> read(String jsonFileName) throws IOException {
        ObjectMapper mapper = createMapper(targetClass);
        final File file = ResourceUtils.getFile("classpath:" + jsonFileName);
        CollectionType listType = mapper.getTypeFactory()
            .constructCollectionType(ArrayList.class, targetClass);
        return mapper.readValue(file, listType);
    }
    
	public ObjectMapper createMapper(Class<T> targetClass) {
        JavaTimeModule dateTimeModule = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer =  
        		new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        dateTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(dateTimeModule);
    	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    	mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    	mapper.addMixIn(targetClass, MixIn.class);
    	for (Class<?> mixin : mixins) {
			mapper.addMixIn(targetClass, mixin);
		}
		return mapper;
	}
}
