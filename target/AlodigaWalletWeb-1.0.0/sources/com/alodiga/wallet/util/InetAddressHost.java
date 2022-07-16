/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.alodiga.wallet.util;
//
//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.util.Enumeration;
//import java.util.logging.Logger;
//import org.apache.log4j.spi.LoggerFactory;
//
//public class InetAddressHost {
//
//    // ** Obtener la dirección del host * /
//    public static String getHostIp(){
//
//        String realIp = null;
//
//        try {
//            InetAddress address = InetAddress.getLocalHost();
//
//            // Si es la dirección de la tarjeta de red de bucle invertido, obtenga la dirección ipv4
//            if (address.isLoopbackAddress()) {
//                address = getInet4Address();
//            }
//
//            realIp = address.getHostAddress();
//            return address.getHostAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return realIp;
//    }
//
//    // ** Obtener la configuración de red IPV4 * /
//    private static InetAddress getInet4Address() throws SocketException {
//        // Obtén toda la información de la tarjeta de red
//        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//        while (networkInterfaces.hasMoreElements()) {
//            NetworkInterface netInterface = (NetworkInterface) networkInterfaces.nextElement();
//            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
//            while (addresses.hasMoreElements()) {
//                InetAddress ip = (InetAddress) addresses.nextElement();
//                if (ip instanceof Inet4Address) {
//                    return ip;
//                }
//            }
//        }
//        return null;
//    }
//}


