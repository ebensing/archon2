package archon2.remote;

import ArchonData.main;
import ArchonData.server.DataService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * User: EJ
 * Date: 11/21/13
 * Time: 9:22 AM
 */
public class DataResource {
    private static DataService client;

    static {
        try {
            Registry reg = LocateRegistry.getRegistry(main.PORT);
            client = (DataService)reg.lookup(main.NAME);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static DataService getClient() {
        return client;
    }
}
