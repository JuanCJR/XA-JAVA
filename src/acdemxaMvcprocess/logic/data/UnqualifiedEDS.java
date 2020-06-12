package acdemxaMvcprocess.logic.data;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UnqualifiedEDS{
	String prefix() default "";
}
