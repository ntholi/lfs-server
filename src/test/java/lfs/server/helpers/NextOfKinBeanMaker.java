
package lfs.server.helpers;

import java.util.Arrays;
import java.util.List;

import lfs.server.mortuary.NextOfKin;

public class NextOfKinBeanMaker extends BeanMaker<NextOfKin>{

	@Override
	protected List<? extends NextOfKin> createBeans() {
		NextOfKin n1 = new NextOfKin(null, "NextOfKin1 names", "NextOfKin1 surname", 
				"father", "+2665781736", "Ha Seoli", null);
		NextOfKin n2 = new NextOfKin(null, "NextOfKin2 names", "NextOfKin2 surname", 
				"mother", "+2665638282", "Marabeng", null);
		return Arrays.asList(n1, n2);
	}

}
