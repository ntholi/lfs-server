package lfs.server.helpers;

import java.util.List;

import lfs.server.transport.Transport;

public class TransportBeans {

	public static List<Transport> transport() {
		Transport t1 = new Transport();
		Transport t2 = new Transport();
		return List.of(t1, t2);
	}

}
