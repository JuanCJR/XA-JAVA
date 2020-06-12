package acdemxaMvcprocess.daoservices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

public class SpringFramework implements ApplicationContextAware {
	public static final String APPLICATION_CONTEXT =
		"com.databorough.xredo.application";
	public static final String BEANFACTORY_SYSTEM_PROPERTY =
		"beanRefContext_xredo.xml";
	static ApplicationContext ctx = null;
	private static final Logger LOGGER =
		LoggerFactory.getLogger(SpringFramework.class);
	private static Map<String, BeanFactoryReference> factoryReferences =
		new HashMap<String, BeanFactoryReference>(150);
	private static BeanFactoryLocator bfLocator = null;

	public static void destroyReferences() {
		Iterator<BeanFactoryReference> i =
			factoryReferences.values().iterator();

		while (i.hasNext()) {
			BeanFactoryReference bfr = i.next();
			bfr.release();
			i.remove();
		}
	}

	public static Object getBean(String beanRef) {
		return ctx.getBean(beanRef);
	}

	public static Object getBean(String factoryName, String beanRef) {
		BeanFactoryLocator bfLocator =
			ContextSingletonBeanFactoryLocator.getInstance(
				"classpath:WEB-INF/beanRefFactory.xml");
		BeanFactoryReference bfReference =
			bfLocator.useBeanFactory(factoryName);
		BeanFactory factory = bfReference.getFactory();

		return factory.getBean(beanRef);
	}

	public static BeanFactoryLocator getBeanFactoryLocator() {
		if (bfLocator == null) {
			bfLocator =
				ContextSingletonBeanFactoryLocator.getInstance(
					BEANFACTORY_SYSTEM_PROPERTY);
			LOGGER.info("Using bean locator factory - " +
				BEANFACTORY_SYSTEM_PROPERTY);
		}

		return bfLocator;
	}

	public static BeanFactoryReference getBeanFactoryReference(
		String factoryName) {
		BeanFactoryReference bf = factoryReferences.get(factoryName);

		if (bf == null) {
			bf = getBeanFactoryLocator().useBeanFactory(factoryName);

			if (bf != null) {
				BeanFactoryReference bfr =
					factoryReferences.put(factoryName, bf);

				if ((bfr != null) && (bfr != bf)) {
					bfr.release();
				}
			}
		}

		return bf;
	}

	/**
	 * Propagate the given to all loaded applcationContext's.
	 */
	public static void publishEvent(ApplicationEvent event) {
		// Iterate over references, and propagate event to each application
		// context found
		Iterator<BeanFactoryReference> i =
			factoryReferences.values().iterator();

		while (i.hasNext()) {
			BeanFactoryReference bfr = i.next();
			BeanFactory beanFactory = bfr.getFactory();

			if (beanFactory instanceof ApplicationContext) {
				ApplicationContext appCtx = (ApplicationContext)beanFactory;
				appCtx.publishEvent(event);
			}
		}
	}

	public void setApplicationContext(ApplicationContext context)
		throws BeansException {
		ctx = context;
	}

	public static void setBeanFactoryLocator(String beanRefAppContextFilename) {
		System.setProperty(BEANFACTORY_SYSTEM_PROPERTY,
			beanRefAppContextFilename);
	}
}
