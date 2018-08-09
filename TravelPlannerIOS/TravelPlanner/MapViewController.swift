//
//  MapViewController.swift
//  TravelPlanner
//
//  Created by mortadha on 25/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import MapKit
class MapViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate {
    let LocationManger = CLLocationManager()
    var nearby = NearbyModel()
    var myRoute : MKRoute!
    @IBOutlet weak var map: MKMapView!
    override func viewDidLoad() {
        super.viewDidLoad()
       
        if Connectivity.isConnectedToInternet() == true {
            
            
            map.delegate = self
            
            map.showsPointsOfInterest = true
            map.showsUserLocation = true
            LocationManger.requestAlwaysAuthorization()
            LocationManger.requestWhenInUseAuthorization()
            if (CLLocationManager.locationServicesEnabled())
            {
                LocationManger.delegate = self
                LocationManger.desiredAccuracy = kCLLocationAccuracyBest
                LocationManger.startUpdatingLocation()
            }
            
            let point1 = MKPointAnnotation()
            let point2 = MKPointAnnotation()
            
            point1.coordinate = CLLocationCoordinate2DMake(LocationManger.location!.coordinate.latitude, LocationManger.location!.coordinate.longitude)
            point1.title = "My Location"
            
            map.addAnnotation(point1)
            print(nearby)
            point2.coordinate = CLLocationCoordinate2DMake(nearby.lat!, nearby.lng!)
            point2.title = nearby.name
            point2.subtitle = nearby.categorie
            map.addAnnotation(point2)
            map.centerCoordinate = point2.coordinate
            
            //Span of the map
            map.setRegion(MKCoordinateRegionMake(point2.coordinate, MKCoordinateSpanMake(0.7,0.7)), animated: true)
            
            let directionsRequest = MKDirectionsRequest()
            let markTaipei = MKPlacemark(coordinate: CLLocationCoordinate2DMake(point1.coordinate.latitude, point1.coordinate.longitude), addressDictionary: nil)
            let markChungli = MKPlacemark(coordinate: CLLocationCoordinate2DMake(point2.coordinate.latitude, point2.coordinate.longitude), addressDictionary: nil)
            
            directionsRequest.source = MKMapItem(placemark: markChungli)
            directionsRequest.destination = MKMapItem(placemark: markTaipei)
            
            directionsRequest.transportType = MKDirectionsTransportType.automobile
            let directions = MKDirections(request: directionsRequest)
            
            directions.calculate(completionHandler: {
                response, error in
                
                if error == nil {
                    self.myRoute = response!.routes[0] as MKRoute
                    self.map.add(self.myRoute.polyline)
                }
            })
            
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
        
      
        
    }
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        
        let myLineRenderer = MKPolylineRenderer(polyline: myRoute.polyline)
        myLineRenderer.strokeColor = UIColor.red
        myLineRenderer.lineWidth = 3
        return myLineRenderer
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
