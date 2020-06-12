package acdemxaMvcprocess.logic.data;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FiPsDsFld {
	String name();
	int fromPos();
	int length() default 0;
	int precision() default 0;
	int scale() default 0;
}
