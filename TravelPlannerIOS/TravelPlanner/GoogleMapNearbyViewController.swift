//
//  GoogleMapNearbyViewController.swift
//  TravelPlanner
//
//  Created by Fadhlaoui Mortadha on 12/25/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import GoogleMaps

class GoogleMapNearbyViewController: UIViewController, CLLocationManagerDelegate {
    var nearby = NearbyModel()
    let locationManager = CLLocationManager()
    var mapView : GMSMapView?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if Connectivity.isConnectedToInternet() {
        
        // Create a GMSCameraPosition that tells the map to display the
        // coordinate -33.86,151.20 at zoom level 6.
        let camera = GMSCameraPosition.camera(withLatitude: locationManager.location!.coordinate.latitude, longitude: locationManager.location!.coordinate.longitude, zoom: 16.0)
        mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        view = mapView
        
        mapView?.isMyLocationEnabled = true
        
        locationManager.requestAlwaysAuthorization()
        locationManager.requestWhenInUseAuthorization()
    
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            locationManager.startUpdatingLocation()
            if Reachability.isLocationServiceEnabled() == true {

        let origin = "\(locationManager.location!.coordinate.latitude),\(locationManager.location!.coordinate.longitude)"
        let destination = "\(String(describing: nearby.lat!)),\(String(describing: nearby.lng!))"
        
        let urlString = "https://maps.googleapis.com/maps/api/directions/json?origin=\(origin)&destination=\(destination)&mode=driving&key=AIzaSyBnDnXfY-nd7BCDlUId78LDFXtFcfhEn0Y"
        
        let url = URL(string: urlString)
        
        URLSession.shared.dataTask(with: url!, completionHandler: {
            (data, response, error) in
            if(error != nil){
                print("error")
            }else{
                DispatchQueue.main.sync() {
                    do{
                        let json = try JSONSerialization.jsonObject(with: data!, options:.allowFragments) as! [String : AnyObject]
                        if json["status"] as! String == "OK"
                        {
                            
                            let routes = json["routes"] as! NSArray
                            self.mapView?.clear()
                            OperationQueue.main.addOperation({
                                
                                for route in routes
                                {
                                    let routeOverviewPolyline:NSDictionary = (route as! NSDictionary).value(forKey: "overview_polyline") as! NSDictionary
                                    let points = routeOverviewPolyline.object(forKey: "points")
                                    let path = GMSPath.init(fromEncodedPath: points! as! String)
                                    let polyline = GMSPolyline.init(path: path)
                                    polyline.strokeWidth = 4
                                    
                                    let bounds = GMSCoordinateBounds(path: path!)
                                    self.mapView?.animate(with: GMSCameraUpdate.fit(bounds, withPadding: 30.0))
                                    
                                    // Creates a marker in the center of the map.
                                    let marker = GMSMarker()
                                    marker.position = CLLocationCoordinate2D(latitude: self.locationManager.location!.coordinate.latitude, longitude: self.locationManager.location!.coordinate.longitude)
                                    marker.title = "My Location"
                                    marker.snippet = "let's go"
                                    marker.map = self.mapView
                                    
                                    // Creates a marker in the center of the map.
                                    let marker1 = GMSMarker()
                                    marker1.position = CLLocationCoordinate2D(latitude: self.nearby.lat!, longitude: self.nearby.lng!)
                                    marker1.title = self.nearby.name
                                    marker1.snippet = self.nearby.categorie
                                    marker1.map = self.mapView
                                    polyline.map = self.mapView
                                    
                                }
                                
                            })
                        }
                        
                    }catch let error as NSError{
                        print("error:\(error)")
                    }}
                
            }
        }).resume()
                
            } else {
                SweetAlert().showAlert("Location Services Disabled", subTitle: "Please enable location services for this app.!", style: AlertStyle.warning, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Ok", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                    if isOtherButton == true {
                        SweetAlert().showAlert("Quit", subTitle: "Sure you want to quit!", style: AlertStyle.error, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Ok", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                            if isOtherButton == true {
                                
                            }
                            else {
                                
                                let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                                
                                let login = storyBoard.instantiateViewController(withIdentifier: "login") as! ViewController
                                
                                self.present(login, animated: true, completion: nil)
                            }
                        }
                    }
                    else {
                        if let url = URL(string: UIApplicationOpenSettingsURLString) {
                            // If general location settings are enabled then open location settings for the app
                            UIApplication.shared.openURL(url)
                            let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                            
                            let nearbylist = storyBoard.instantiateViewController(withIdentifier: "nearbylist") as! NearbyThingsViewController
                            
                            self.show(nearbylist, sender:true)
                        }
                        
                    }
                }
            }
            
            
        }else{
    
            SweetAlert().showAlert("No internet connection", subTitle: "", style: AlertStyle.warning, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Open network!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    SweetAlert().showAlert("Quit", subTitle: "Sure you want to quit!", style: AlertStyle.error, buttonTitle:"Cancel", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Ok", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                        if isOtherButton == true {
                            
                        }
                        else {
                            
                            let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                            
                            let login = storyBoard.instantiateViewController(withIdentifier: "login") as! ViewController
                            
                            self.present(login, animated: true, completion: nil)
                        }
                    }
                }
                else {
                    let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                    let app = UIApplication.shared
                    app.openURL(url!)
                    let storyBoard : UIStoryboard = UIStoryboard(name: "Main", bundle:nil)
                    
                    let nearbylist = storyBoard.instantiateViewController(withIdentifier: "nearbylist") as! NearbyThingsViewController
                    
                    self.show(nearbylist, sender:true)
                }
            }
        }
        
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last
        
        let camera = GMSCameraPosition.camera(withLatitude: (location?.coordinate.latitude)!, longitude: (location?.coordinate.longitude)!, zoom: 17.0)
        
        self.mapView?.animate(to: camera)
        
        //Finally stop updating location otherwise it will come again and again in this delegate
        self.locationManager.stopUpdatingLocation()
        
    }
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
