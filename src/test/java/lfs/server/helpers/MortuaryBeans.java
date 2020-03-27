package lfs.server.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import lfs.server.branch.District;
import lfs.server.mortuary.Corpse;
import lfs.server.mortuary.Corpse.Gender;
import lfs.server.mortuary.NextOfKin;
import lfs.server.mortuary.OtherMortuary;
import lfs.server.transport.Transport;

public class MortuaryBeans {

	public static List<Corpse> corpse() {
		List<NextOfKin> nextOfKins = nextOfKin();
		Transport transport = TransportBeans.transport().get(0);
		Corpse c1 = new Corpse(null, "corpseName", "corpseSurname", Gender.MALE, 
				LocalDate.of(1953, 10, 1), "Ha Moho, Thabana Morena", 
				District.Mafeteng, "Ntate Rasehloho", nextOfKins, 
				LocalDate.now(), LocalDateTime.now(), "Natural Causes", 
				"At Home", "F3", "BNG-26", "Molise", transport, 
				"There are no special requirements", "No other notes", false, null, 
				new OtherMortuary("MKM"));
		return Arrays.asList(c1);
	}
	
	public static List<NextOfKin> nextOfKin() {
		NextOfKin n1 = new NextOfKin(null, "NextOfKin1 names", "NextOfKin1 surname", 
				"father", "+2665781736", "Ha Seoli", null);
		NextOfKin n2 = new NextOfKin(null, "NextOfKin2 names", "NextOfKin2 surname", 
				"mother", "+2665638282", "Marabeng", null);
		return Arrays.asList(n1, n2);
	}
}
