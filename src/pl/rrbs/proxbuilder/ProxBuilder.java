package pl.rrbs.proxbuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import javax.naming.OperationNotSupportedException;

public class ProxBuilder {
	
	@SuppressWarnings("unchecked")
	public static <TInterface extends IProxBuilder<? extends Object>> 
	TInterface create(Class<? extends TInterface> clazz) {
		return (TInterface) Proxy.newProxyInstance(
				clazz.getClassLoader(), 
				new Class<?>[]{clazz},
				new BuilderInvokationHandler<>(clazz));
	}

	
	private static class BuilderInvokationHandler
	<TInterface extends IProxBuilder<? extends Object>> implements InvocationHandler {
		private Class<?> entityClass;
		private Object entity;
		
		public BuilderInvokationHandler(Class<? extends TInterface> clazz) {
			try {
				entityClass = getEnityClass(clazz);
				//TODO get declared constr
				Constructor<?> constructor = entityClass.getConstructor();
				constructor.setAccessible(true);
				entity = constructor.newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
		}

		private Class<?> getEnityClass(Class<? extends TInterface> clazz) {
			ParameterizedType interfaceClass = (ParameterizedType) Arrays.stream(clazz.getGenericInterfaces())
				.filter(iface -> iface.getTypeName().startsWith(IProxBuilder.class.getTypeName()+ "<"))
				.findAny().get();
			Class<?> entityClass = (Class<?>) interfaceClass.getActualTypeArguments()[0];
			return entityClass;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			if (methodName.startsWith("set")) {
				String fieldName = methodName.substring("set".length());
				fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
				
				Field field = entity.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(entity, args[0]);
				
				return proxy;
			} else if (methodName.equals("build")) {
				return entity;
			}
			return new OperationNotSupportedException(methodName);
		}
		
	}
}
