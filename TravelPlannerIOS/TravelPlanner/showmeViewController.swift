//
//  showmeViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 09/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import GoogleMaps
import GooglePlaces
import Alamofire
import SwiftyJSON
class showmeViewController: UIViewController,GMSMapViewDelegate,CLLocationManagerDelegate {

    @IBOutlet weak var mymapview: GMSMapView!
    var lat:Double = Double()
    var lon:Double = Double()
    var LocationManager = CLLocationManager()
    var locationStart = CLLocation(latitude: 36.898033, longitude: 10.189533)
    var locationEnd = CLLocation()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        if Connectivity.isConnectedToInternet() == true {
            
            
            locationEnd = CLLocation(latitude: lat, longitude: lon)
            LocationManager = CLLocationManager()
            LocationManager.delegate = self
            LocationManager.requestWhenInUseAuthorization()
            LocationManager.startUpdatingLocation()
            LocationManager.startMonitoringSignificantLocationChanges()
            locationStart = LocationManager.location!
            
            
            initGoogleMpas()
            //36.898033
            //10,189533
            createMarker(titleMarker: "Start location",  latitude: locationStart.coordinate.latitude, longitude: locationStart.coordinate.longitude)
            
            createMarker(titleMarker: "Your saved place",  latitude: (locationEnd.coordinate.latitude), longitude: (locationEnd.coordinate.longitude))
            self.drawPath(startLocation: locationStart, endLocation: locationEnd)
            
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
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    func initGoogleMpas(){
        let camera = GMSCameraPosition.camera(withLatitude: locationStart.coordinate.latitude, longitude: locationStart.coordinate.longitude, zoom: 12.0)
        let mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.isMyLocationEnabled = true
        self.mymapview.camera = camera
        self.mymapview.delegate = self
        self.mymapview.isMyLocationEnabled = true
        self.mymapview.settings.myLocationButton = true
        
        // Creates a marker in the center of the map.
        
    }
    func createMarker(titleMarker: String,latitude: CLLocationDegrees,longitude: CLLocationDegrees	){
    let Marker = GMSMarker()
        Marker.position = CLLocationCoordinate2DMake(latitude,longitude)
        Marker.title = titleMarker

        Marker.map = mymapview
    }
    
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("error when get location")
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let location = locations.last
        
        //		let camera = GMSCameraPosition.camera(withLatitude: (location?.coordinate.latitude)!, longitude: (location?.coordinate.longitude)!, zoom: 17.0)
        
        let locationTujuan = CLLocation(latitude: 37.784023631590777, longitude: -122.40486681461333)
        
        
        
        drawPath(startLocation: location!, endLocation: locationTujuan)
        
        //		self.googleMaps?.animate(to: camera)
        self.LocationManager.stopUpdatingLocation()
        
    }
    
    // MARK: - GMSMapViewDelegate
    
    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        mymapview.isMyLocationEnabled = true
    }
    
    func mapView(_ mapView: GMSMapView, willMove gesture: Bool) {
        mymapview.isMyLocationEnabled = true
        
        if (gesture) {
            mapView.selectedMarker = nil
        }
    }
    
    func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        mymapview.isMyLocationEnabled = true
        return false
    }
    
    func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        print("COORDINATE \(coordinate)") // when you tapped coordinate
    }
    
    func didTapMyLocationButton(for mapView: GMSMapView) -> Bool {
        mymapview.isMyLocationEnabled = true
        mymapview.selectedMarker = nil
        return false
    }
    
    
    
    //MARK: - this is function for create direction path, from start location to desination location
    
    func drawPath(startLocation: CLLocation, endLocation: CLLocation)
    {
        let origin = "\(startLocation.coordinate.latitude),\(startLocation.coordinate.longitude)"
        let destination = "\(endLocation.coordinate.latitude),\(endLocation.coordinate.longitude)"
        
        
        let url = "https://maps.googleapis.com/maps/api/directions/json?origin=\(origin)&destination=\(destination)&mode=driving"
        
        Alamofire.request(url).responseJSON { response in
            
            print(response.request as Any)  // original URL request
            print(response.response as Any) // HTTP URL response
            print(response.data as Any)     // server data
            print(response.result as Any)   // result of response serialization
            
            let json = try? JSON(data: response.data!)
            
                let routes = json?["routes"].arrayValue
            
            // print route using Polyline
            for route in routes!
            {
                let routeOverviewPolyline = route["overview_polyline"].dictionary
                let points = routeOverviewPolyline?["points"]?.stringValue
                let path = GMSPath.init(fromEncodedPath: points!)
                let polyline = GMSPolyline.init(path: path)
                polyline.strokeWidth = 4
                polyline.strokeColor = UIColor.red
                polyline.map = self.mymapview
            }
            
        }
    }

    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
