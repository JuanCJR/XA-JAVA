package acdemxaMvcprocess.data;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Recfmt {
	String recfname();
}
