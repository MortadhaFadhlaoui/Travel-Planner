//
//  AccueilViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 08/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import AACarousel
import Kingfisher
import CoreLocation
import CoreData
class AccueilViewController: UIViewController ,AACarouselDelegate,CLLocationManagerDelegate{
    func didSelectCarouselView(_ view: AACarousel, _ index: Int) {
        
    }
    
    func callBackFirstDisplayView(_ imageView: UIImageView, _ url: [String], _ index: Int) {
        
    }
    
var people:[NSManagedObject] = []
 
    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var citynameee: UILabel!
    @IBOutlet weak var firstimg: UIImageView!
  
  
    
    
    @IBOutlet weak var weatherimg: UIImageView!
    
    @IBOutlet weak var lbldescription: UILabel!
    @IBOutlet weak var lblvalue: UILabel!
   
    @IBOutlet weak var firstaa: AACarousel!
    @IBOutlet var all: UIView!
    @IBOutlet weak var fullview: UIView!
    @IBOutlet weak var leadindconsraint: NSLayoutConstraint!
    var menus = false
    var myloc = CLLocation()
    let locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        
        
        super.viewDidLoad()
        
        
        
        if Connectivity.isConnectedToInternet() == true {
            
            
            
            
            load.startAnimating()
            self.citynameee.isHidden = true
            self.weatherimg.isHidden = true
            self.lblvalue.isHidden = true
            self.lbldescription.isHidden = true
            self.firstimg.isHidden = true
            self.firstaa.isHidden = true
            print(SignUpViewController.tokensec)
            if CLLocationManager.locationServicesEnabled() {
                locationManager.delegate = self
                locationManager.desiredAccuracy = kCLLocationAccuracyBest // You can change the locaiton accuary here.
                locationManager.startUpdatingLocation()
                
            }
            let mylat:String
            let mylon:String
            var mycityn:String = String()
            
            if(locationManager.location?.coordinate == nil){
                mylat = "36.897898"
                mylon = "10.189599"
                
            }else{
                mylat = String(format:"%f", (locationManager.location?.coordinate.latitude)!)
                mylon = String(format:"%f", (locationManager.location?.coordinate.longitude)!)
                
            }
            
            let urlPath1 = URL(string: "http://api.openweathermap.org/data/2.5/weather?lat="+mylat+"&lon="+mylon+"&APPID=d9029912d54db76cc706263538e03769&units=metric")
            
            Alamofire.request(urlPath1!,method:.post).responseJSON { responsep1 in
                if(responsep1.result.isSuccess){
                    // let userm = UserModel()
                    
                    let cast = responsep1.result.value! as? Dictionary<String, AnyObject>
                    let cast1 = cast?["main"] as? Dictionary<String, AnyObject>
                    let castr = cast?["sys"] as? Dictionary<String, AnyObject>
                    let cr = castr?["country"] as? String
                    let vr = cast!["name"] as? String
                    
                    self.citynameee.text = vr!+", "+cr!
                    
                    let temper = cast1?["temp"] as? Double
                    let temppp = Int(temper!)
                    self.lblvalue.text =  "\(temppp)°"
                    
                    let cast11 = cast?["weather"] as? NSArray
                    let obj1 = cast11?[0] as? Dictionary<String, AnyObject>
                    let gg = obj1!["description"] as! String
                    let ggwp = obj1!["icon"] as! String
                    self.weatherimg.image = UIImage(named: ggwp)
                    
                    self.lbldescription.text = gg
                    
                    
                }else if(responsep1.result.isFailure){
                    print("le le")
                }
                
            }
            
            
            
            
            
            
            let urlPath69 = URL(string: "https://maps.googleapis.com/maps/api/geocode/json?latlng="+mylat+","+mylon+"&key=AIzaSyA7as47XAgn2k0LX_DN8kr0TiDxOzqks38&language=en")
            Alamofire.request(urlPath69!,method:.post).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp!["results"] as? NSArray
                    let dictp2 = dictp1![0] as? Dictionary<String, AnyObject>
                    let dictp3 = dictp2!["address_components"] as? NSArray
                    let dictp4 = dictp3![4] as? Dictionary<String, AnyObject>
                    let dictp5 = dictp4!["long_name"] as? String
                    mycityn =  dictp5!
                    
                    let urlPath = URL(string: "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q="+mycityn+"&image_type=photo")
                    Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            // let userm = UserModel()
                            let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                            let groups = dictp?["hits"] as? NSArray
                            var array = [String]()
                            var arraynames = [String]()
                            for places in (groups! as NSArray) {
                                let obj = places as? Dictionary<String, AnyObject>
                                array.append(obj!["webformatURL"] as! String)
                                arraynames.append(obj!["tags"] as! String)
                                
                                
                            }
                            let url = URL(string:array[0])
                            let data = try? Data(contentsOf: url!)
                            let img1: UIImage = UIImage(data: data!)!
                            self.firstimg.image = img1
                            
                            
                            print(array.count)
                            var titleArray = [String]()
                            let pathArray = [array[6],array[7],array[3],array[8],array[5],array[1]]
                            titleArray = [arraynames[6],arraynames[7],arraynames[3],arraynames[8],arraynames[5],arraynames[1]]
                            
                            
                            self.firstaa.delegate = self
                            self.firstaa.setCarouselData(paths: pathArray,  describedTitle: titleArray, isAutoScroll: true, timer: 3.0, defaultImage: "defaultImage")
                            //optional methods
                            self.firstaa.setCarouselOpaque(layer: false, describedTitle: false, pageIndicator: false)
                            self.firstaa.setCarouselLayout(displayStyle: 1, pageIndicatorPositon: 2, pageIndicatorColor: nil, describedTitleColor: nil, layerColor: nil)
                            self.fullview.layer.shadowOpacity=1
                            self.fullview.layer.shadowRadius = 6
                            self.load.isHidden = true
                            self.citynameee.isHidden = false
                            self.weatherimg.isHidden = false
                            self.lblvalue.isHidden = false
                            self.lbldescription.isHidden = false
                            self.firstimg.isHidden = false
                            self.firstaa.isHidden = false
                            
                            
                        }else if(responsep.result.isFailure){
                            print("le le")
                        }
                        
                    }
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
            
            
            
            
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
   
    func downloadImages(_ url: String, _ index: Int) {
        let imageView = UIImageView()
        imageView.kf.setImage(with: URL(string: url)!, placeholder: UIImage.init(named: "defaultImage"), options: [.transition(.fade(0))], progressBlock: nil, completionHandler: { (downloadImage, error, cacheType, url) in
            if((downloadImage) != nil){
                self.firstaa.images[index] = downloadImage!
            }else{
                
            }
            
        })
        
        
        
        
    }
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.first {
            myloc = location
           
        }
    }
    
    // If we have been deined access give the user the option to change it
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if(status == CLAuthorizationStatus.denied) {
            showLocationDisabledPopUp()
        }
    }
    
    // Show the popup to the user if we have been deined access
    func showLocationDisabledPopUp() {
        let alertController = UIAlertController(title: "Background Location Access Disabled",
                                                message: "In order to use this app we need your location",
                                                preferredStyle: .alert)
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        alertController.addAction(cancelAction)
        
        let openAction = UIAlertAction(title: "Open Settings", style: .default) { (action) in
            if let url = URL(string: UIApplicationOpenSettingsURLString) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            }
        }
        alertController.addAction(openAction)
        
        self.present(alertController, animated: true, completion: nil)
    }
    
    @IBAction func logout(_ sender: Any) {

        let urlPath = URL(string: NavigatorData.URL+"/api/logout?token="+SignUpViewController.tokensec)
        Alamofire.request(urlPath!,method:.get ).responseJSON { responsep in
            if(responsep.result.isSuccess){
                
                let context = ( UIApplication.shared.delegate as! AppDelegate ).persistentContainer.viewContext
                let deleteFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "User")
                let deleteRequest = NSBatchDeleteRequest(fetchRequest: deleteFetch)
                do
                {
                    try context.execute(deleteRequest)
                    try context.save()
                    print("fasa5t")
                }
                catch
                {
                    print ("There was an error")
                }
                self.save(name: SignUpViewController.connecteduser.username!, pwd: "")
                
                
                
                let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let newViewController = storyBoard.instantiateViewController(withIdentifier: "login") 
                self.present(newViewController, animated: true, completion: nil)
            }else if(responsep.result.isFailure){
                print("le le")
            }
            
        }
    }
    @IBAction func menu(_ sender: Any) {
        if (menus == false) {
            leadindconsraint.constant = 0
            UIView.animate(withDuration: 0.3, animations:{
            self.view.layoutIfNeeded()
            })
            
            menus=true
        }else{
            leadindconsraint.constant = -250
            
            UIView.animate(withDuration: 0.3, animations:{
                self.view.layoutIfNeeded()
            })

            menus=false
    }
}
    override func viewWillAppear(_ animated: Bool) {
        if Connectivity.isConnectedToInternet() == true {
            
            
            
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
    func save(name: String,pwd: String) {
        
        guard let appDelegate =
            UIApplication.shared.delegate as? AppDelegate else {
                return
        }
        
        // 1
        let managedContext =
            appDelegate.persistentContainer.viewContext
        
        // 2
        let entity =
            NSEntityDescription.entity(forEntityName: "User",
                                       in: managedContext)!
        
        let person = NSManagedObject(entity: entity,
                                     insertInto: managedContext)
        
        // 3
        person.setValue(name, forKeyPath: "username")
        person.setValue(pwd, forKeyPath: "password")
        
        // 4
        do {
            try managedContext.save()
            people.append(person)
        } catch let error as NSError {
            print("Could not save. \(error), \(error.userInfo)")
        }
    }
    
}
