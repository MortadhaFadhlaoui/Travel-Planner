//
//  UserProtocol.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 15/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit

import Foundation

protocol UserModelProtocol: class {
    func itemsDownloaded(items: NSArray)
}


class UserProtocol: NSObject, URLSessionDataDelegate {
    
    //properties
    
    weak var delegate: UserModelProtocol!
    
    var data = Data()
    
    

    func downloadItems(username: String,password: String) {
    let urlPath: String = "http://192.168.1.5/TravelPlannerIOS/login.php?username="+username+"&password="+password
    let url: URL = URL(string: urlPath)!
        
    let defaultSession = Foundation.URLSession(configuration: URLSessionConfiguration.default)
    
    let task = defaultSession.dataTask(with: url) { (data, response, error) in
        
        if error != nil {
            print("Failed to download data")
        }else {
            print("Data downloaded")
           
            self.parseJSON(data!)
            
            
        }
        
    }
    
    task.resume()
}

    func parseJSON(_ data:Data) {
        
        var jsonResult = NSArray()
        
        do{
            jsonResult = try JSONSerialization.jsonObject(with: data, options:JSONSerialization.ReadingOptions.allowFragments) as! NSArray
           
        } catch let error as NSError {
            print(error)
            
        }
        
        var jsonElement = NSDictionary()
        let locations = NSMutableArray()
        
        for i in 0 ..< jsonResult.count
        {
            
            jsonElement = jsonResult[i] as! NSDictionary
            
            
            let location = UserModel()
            
            if let nom = jsonElement["nom"] as? String {
                location.nom = jsonElement["nom"] as! String
            }
            if let prenom = jsonElement["prenom"] as? String {
                location.prenom = jsonElement["prenom"] as! String            }
            
            if let image = jsonElement["image"] as? String {
               location.image = jsonElement["image"] as! String
            }
            
            
                let ok = jsonElement["id"] as! String
                location.idUser = Int(ok)!
                location.username = jsonElement["username"] as! String
                location.password = jsonElement["password"] as! String
                location.email = jsonElement["email"] as! String
            
            location.role = jsonElement["role"] as! String
                
            
            
            locations.add(location)
           
            
        }
        
        DispatchQueue.main.async(execute: { () -> Void in
            
            self.delegate.itemsDownloaded(items: locations)
            
        })
    }

}
