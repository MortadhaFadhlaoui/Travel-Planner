//
//  SavedPlacesListViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 29/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import CoreLocation
import GoogleMaps
import GooglePlacePicker
import GooglePlaces
import Alamofire
class SavedPlacesListViewController: UIViewController,CLLocationManagerDelegate,GMSMapViewDelegate ,GMSAutocompleteViewControllerDelegate{

    @IBOutlet weak var GoogleMapsView: GMSMapView!
    var lat: Double!
    var long: Double!
    var name: String!
    var LocationManager = CLLocationManager()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if Connectivity.isConnectedToInternet() == true {
            LocationManager = CLLocationManager()
            LocationManager.delegate = self
            LocationManager.requestWhenInUseAuthorization()
            LocationManager.startUpdatingLocation()
            LocationManager.startMonitoringSignificantLocationChanges()
            
            
            initGoogleMpas()
            
            
            
        }else{
            
            SweetAlert().showAlert("Conncetion error?", subTitle: "You have no internet acces!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Enable WIFI!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "login")
                    self.present(newViewController, animated: true, completion: nil)
                    
                }
                else {
                    let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                    let app = UIApplication.shared
                    app.open(url!, options: [:], completionHandler: nil)
                    
                }
            }
            
            
            
            
            
            
        }
        
        
        // Do any additional setup after loading the view.
    }

   func initGoogleMpas(){
    let camera = GMSCameraPosition.camera(withLatitude: -33.86, longitude: 151.20, zoom: 6.0)
    let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
    mapView.isMyLocationEnabled = true
    self.GoogleMapsView.camera = camera
    self.GoogleMapsView.delegate = self
    self.GoogleMapsView.isMyLocationEnabled = true
    self.GoogleMapsView.settings.myLocationButton = true
    
    // Creates a marker in the center of the map.
    
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("error when get location")
    }
    
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last
        let camera = GMSCameraPosition.camera(withLatitude: (location?.coordinate.latitude)!, longitude: (location?.coordinate.longitude)!, zoom: 17)
        self.GoogleMapsView.animate(to: camera)
        self.LocationManager.stopUpdatingLocation()
        self.lat = location?.coordinate.latitude
        self.long = location?.coordinate.longitude
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: (location?.coordinate.latitude)!, longitude: (location?.coordinate.longitude)!)
        marker.title = "Your location"
        marker.snippet = "Your location"
        marker.map = self.GoogleMapsView
        
    }
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        
        self.GoogleMapsView.isMyLocationEnabled = true
    }
    
    
    func mapView(_ mapView: GMSMapView, willMove gesture: Bool) {
        self.GoogleMapsView.isMyLocationEnabled = true
        if(gesture){
             mapView.selectedMarker = nil
            }
    }
    
    
    func viewController(_ viewController: GMSAutocompleteViewController, didAutocompleteWith place: GMSPlace) {
        self.GoogleMapsView.clear()
        let camera = GMSCameraPosition.camera(withLatitude: place.coordinate.latitude, longitude: place.coordinate.longitude, zoom: 15.0)
        self.GoogleMapsView.camera = camera
        self.dismiss(animated: true, completion: nil)
        self.lat = place.coordinate.latitude
        self.long = place.coordinate.longitude
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: (place.coordinate.latitude), longitude: (place.coordinate.longitude))
        marker.title = "Your location"
        marker.snippet = "Your location"
        marker.map = self.GoogleMapsView
    }
    
    
    func viewController(_ viewController: GMSAutocompleteViewController, didFailAutocompleteWithError error: Error) {
        print("error in autocomplete")
    }
    
    
    func wasCancelled(_ viewController: GMSAutocompleteViewController) {
        self.dismiss(animated: true, completion: nil)
    }
    
    
    
    @IBAction func openSearchLocation(_ sender: UIBarButtonItem) {
        let autocompletecontroller = GMSAutocompleteViewController()
        autocompletecontroller.delegate = self
        self.LocationManager.startUpdatingLocation()
        self.present(autocompletecontroller, animated: true, completion: nil)
        
    }
    @IBAction func saveplace(_ sender: UIBarButtonItem) {
        let alertController = UIAlertController(title: "Add New Saved Place", message: "", preferredStyle: UIAlertControllerStyle.alert)
        let saveAction = UIAlertAction(title: "Save", style: UIAlertActionStyle.default, handler: {
            alert -> Void in
            
            let firstTextField = alertController.textFields![0] as UITextField
            
            let name = "saved place"
            let cat = "Selected on map"
            let lat = self.lat
            let lng = self.long
            let urlPath = URL(string: NavigatorData.URL+"/api/save")
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
            let parameters = [
                "title" : firstTextField.text!,
                "name" : name,
                "categorie" : cat,
                "lat" : lat as Any,
                "log" : lng as Any] as [String : Any]
            
            print(urlPath!)
            Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    //let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    
                    let alert = UIAlertController(title: firstTextField.text!, message: "Added successfuly", preferredStyle: UIAlertControllerStyle.alert)
                    alert.addAction(UIAlertAction(title: "ok", style: UIAlertActionStyle.default, handler: nil))
                    self.present(alert, animated: true, completion: nil)
                    /*  let success = dictp?["success"]! as? Int
                     if(success?.hashValue==1){
                     
                     }else{
                     
                     }
                     
                     let image = user?["image"] as? String
                     let nom = user?["nom"] as? String
                     let prenom = user?["prenom"] as? String
                     let username = user?["username"] as? String
                     let role = user?["role"] as? String
                     let numtel = user?["numtel"] as? String
                     let email = user?["email"] as? String
                     let token = data?["token"] as? String*/
                    
                    
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
        })

        
        alertController.addTextField(configurationHandler: ) { (textField : UITextField!) -> Void in
            textField.placeholder = "Enter Title"
        }

        let cancelAction = UIAlertAction(title: "CANCEL", style: .default)
        
        alertController.addAction(saveAction)
        alertController.addAction(cancelAction)
        present(alertController,animated:true)
    }
    
}
