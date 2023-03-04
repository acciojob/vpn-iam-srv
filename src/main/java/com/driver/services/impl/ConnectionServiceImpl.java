package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import javassist.bytecode.ExceptionsAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.monitor.CounterMonitorMBean;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{

        User user = userRepository2.findById(userId).get();
        if(user.getMaskedIP() != null){
            throw new Exception("Already connected");
        }else if(user.getCountry().getCountryName().toString().equals(countryName)){
            return user;
        }else{
            if(user.getServiceProviderList() == null){
                throw new Exception("Unable to connect");
            }

            List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
            int a  = Integer.MAX_VALUE;
            ServiceProvider serviceProvider = null;
            Country country = null;

            for(ServiceProvider serviceProvider1 : serviceProviderList){
                List<Country> countryList = serviceProvider1.getCountryList();

                for(Country country1  : countryList){
                    if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && a > serviceProvider1.getId()){
                        a = serviceProvider1.getId();
                        serviceProvider = serviceProvider1;
                        country  = country1;
                    }
                }
            }

            if(serviceProvider != null){
                Connection connection = new Connection();
                connection.setUser(user);
                connection.setServiceProvider(serviceProvider);

                String cc = country.getCode();
                int givenId = serviceProvider.getId();
                String mask = cc+"."+givenId+"."+userId;

                user.setMaskedIP(mask);
                user.setConnected(true);
                user.getConnectionList().add(connection);

                serviceProvider.getConnectionList().add(connection);

                userRepository2.save(user);
                serviceProviderRepository2.save(serviceProvider);
            }

        }
        return user;

    }
    @Override
    public User disconnect(int userId) throws Exception {
         User user = userRepository2.findById(userId).get();
         if(user.getConnected() == false){
             throw new Exception("Already disconnected");
         }

         user.setMaskedIP(null);
         user.setConnected(false);
         userRepository2.save(user);

         return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {

        User user = userRepository2.findById(senderId).get();
        User user1 = userRepository2.findById(receiverId).get();

        if(user1.getMaskedIP()!=null){
            String str = user1.getMaskedIP();
            String cc = str.substring(0,3); //chopping country code = cc

            if(cc.equals(user.getCountry().getCode()))
                return user;
            else {
                String countryName = "";

                if (cc.equalsIgnoreCase(CountryName.IND.toCode()))
                    countryName = CountryName.IND.toString();
                if (cc.equalsIgnoreCase(CountryName.USA.toCode()))
                    countryName = CountryName.USA.toString();
                if (cc.equalsIgnoreCase(CountryName.JPN.toCode()))
                    countryName = CountryName.JPN.toString();
                if (cc.equalsIgnoreCase(CountryName.CHI.toCode()))
                    countryName = CountryName.CHI.toString();
                if (cc.equalsIgnoreCase(CountryName.AUS.toCode()))
                    countryName = CountryName.AUS.toString();

                User user2 = connect(senderId,countryName);
                if (!user2.getConnected()){
                    throw new Exception("Cannot establish communication");

                }
                else return user2;
            }

        }
        else{
            if(user1.getCountry().equals(user.getCountry())){
                return user;
            }
            String countryName = user1.getCountry().getCountryName().toString();
            User user2 =  connect(senderId,countryName);
            if (!user2.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            else return user2;

        }
    }

    }

