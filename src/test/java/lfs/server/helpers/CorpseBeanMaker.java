package lfs.server.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import lfs.server.branch.District;
import lfs.server.mortuary.Corpse;
import lfs.server.mortuary.Corpse.Gender;
import lfs.server.mortuary.NextOfKin;
import lfs.server.transport.Transport;

public class CorpseBeanMaker extends BeanMaker<Corpse> {

	@Override
	protected List<? extends Corpse> createBeans() {
		List<NextOfKin> nextOfKins = Arrays.asList(new NextOfKinBeanMaker().getAny());
		Transport transport = null;// TODO new TransportBeanMaker().getAny();
		Corpse c1 = new Corpse(null, "corpseName", "corpseSurname", Gender.MALE, 
				LocalDate.of(1953, 10, 1), "Ha Moho, Thabana Morena", 
				District.Mafeteng, "Ntate Rasehloho", nextOfKins, 
				LocalDate.now(), LocalDateTime.now(), "Natural Causes", 
				"At Home", "F3", "BNG-26", "Molise", transport, 
				"There are no special requirements", "No other notes", false, null, 
				"Shopong");
		return Arrays.asList(c1);
	}
}
