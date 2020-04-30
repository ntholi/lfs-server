package com.breakoutms.lfs.server.persistence;

import java.io.Serializable;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import com.breakoutms.lfs.server.branch.CurrentBranch;
import com.breakoutms.lfs.server.util.BeanUtil;
import com.breakoutms.lfs.server.util.TypeUtils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class IdGenerator extends SequenceStyleGenerator {

	public static final String STRATEGY = "com.breakoutms.lfs.server.persistence.IdGenerator";
	public static final String ID_TYPE_STRING = "java.lang.String";
	public static final String ID_TYPE_INTEGER = "java.lang.Integer";
	public static final String ID_TYPE_LONG = "java.lang.Long";
	
    public static final String ID_TYPE_PARAM = "ID_TYPE_PARAM";
    public static final String ID_TYPE_DEFAULT = ID_TYPE_INTEGER;
	private String idType;
     
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
    	Serializable id = super.generate(session, object);
    	String result = currentBranch().get().getSyncNumber() 
    			+ StringUtils.leftPad(String.valueOf(id), 6, '0');
        Class<?> type = String.class;
        
        try {
			type = Class.forName(idType);
		} catch (ClassNotFoundException e) {
			Class<?> objType = object != null? object.getClass() : null;
			log.error("Unable to determin id type for '"+ objType+"', provided id value: "+ idType, e);
		}
        return (Serializable) TypeUtils.castType(result, type);
    }
     
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        idType = ConfigurationHelper.getString(ID_TYPE_PARAM, params, ID_TYPE_DEFAULT);
    }
 
    private CurrentBranch cb;
	public CurrentBranch currentBranch() {
		if(cb == null) {
			cb = BeanUtil.getBean(CurrentBranch.class);
		}
		return cb;
	}
}
