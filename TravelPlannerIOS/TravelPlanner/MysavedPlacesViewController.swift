//
//  MysavedPlacesViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 09/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
import GoogleMaps
import GooglePlaces
class MysavedPlacesViewController: UIViewController ,UITableViewDataSource,UITableViewDelegate,CLLocationManagerDelegate{
    var feedItems = [Dictionary<String, AnyObject>]()
    var details: showmeViewController?
    
    @IBOutlet weak var tableview: UITableView!
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    override func viewWillAppear(_ animated: Bool) {
       print(SignUpViewController.connecteduser.idUser)
        
        if Connectivity.isConnectedToInternet() == true {
             load.startAnimating()
            feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let ff = String(describing: SignUpViewController.connecteduser.idUser!)
            let urlPath = URL(string: NavigatorData.URL+"/api/Getmysaved?id="+ff)
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["saveds"] as? NSArray
                    if(dictp1 != nil){
                        
                        
                        for places in (dictp1! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            self.feedItems.append(place!)
                            self.tableview.reloadData()
                        }
                    }
                    if(self.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no saved places!", style: AlertStyle.error)
                    }
                    
                    self.load.isHidden = true
                    
                    
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
        
      
        
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableview.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
            self?.feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let ff = String(describing: SignUpViewController.connecteduser.idUser!)
            let urlPath = URL(string: NavigatorData.URL+"/api/Getmysaved?id="+ff)
            print(ff)
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["saveds"] as? NSArray
                    if(dictp1 != nil){
                        
                    
                    for places in (dictp1! as NSArray) {
                        let place = places as? Dictionary<String, AnyObject>
                        self?.feedItems.append(place!)
                        self?.tableview.reloadData()
                    }
                    }
                    if(self?.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no saved places!", style: AlertStyle.error)
                    }
                    
                    self?.tableview.layoutIfNeeded()
                    self?.tableview.beginUpdates()
                    self?.tableview.endUpdates()
                    self?.tableview.cr.endHeaderRefresh()
                    
                    
                    
                }else if(responsep.result.isFailure){
                    print("le le")
                }
                
            }
            
            }
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return feedItems.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "mysavedcell", for: indexPath)
        
        let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        
        let lblName:UILabel = cell.viewWithTag(2) as! UILabel
        lblName.text = nearby["title"] as! String?
        let lblcategorie:UILabel = cell.viewWithTag(3) as! UILabel
        lblcategorie.text = nearby["name"] as! String?
        
        let lblctext:UILabel = cell.viewWithTag(4) as! UILabel
        
        lblctext.text = nearby["categorie"] as! String?
        let imgv:UIImageView = cell.viewWithTag(1) as! UIImageView
        let cattype = nearby["categorie"] as! String?
        if(cattype == "Selected on map"){
            
            imgv.image = #imageLiteral(resourceName: "green")
            
        }else {
        
            imgv.image = #imageLiteral(resourceName: "foursquare")
            
        }
        
        
        
        return cell
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "showme" {
            
            let index : Int = (tableview.indexPathForSelectedRow?.row)!
            
            self.details = segue.destination as? showmeViewController
            let nearby = feedItems[index]
            self.details?.lat = nearby["lat"] as! Double
            self.details?.lon = nearby["log"] as! Double
            
            
        }
        
    }
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if CLLocationManager.locationServicesEnabled() {
            switch CLLocationManager.authorizationStatus() {
            case .notDetermined, .restricted, .denied:
                SweetAlert().showAlert("Location error?", subTitle: "Please enable your location services!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Enable Location!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                    if isOtherButton == true {
                        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                        let newViewController = storyBoard.instantiateViewController(withIdentifier: "login")
                        self.present(newViewController, animated: true, completion: nil)
                        
                    }
                    else {
                        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                        let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthomee")
                        self.show(newViewController, sender: true)
                        let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                        let app = UIApplication.shared
app.open(url!, options: [:], completionHandler: nil)
                    }
                }
                return false
            case .authorizedAlways, .authorizedWhenInUse:
                print("mrigel")
                return true
            }
        } else {
            SweetAlert().showAlert("Location error?", subTitle: "Please enable your location services!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Enable Location!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "login")
                    self.present(newViewController, animated: true, completion: nil)
                    
                }
                else {
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthomee")
                    self.show(newViewController, sender: true)
                    let url = URL(string: "App-Prefs:root=WIFI") //for WIFI setting app
                    let app = UIApplication.shared
app.open(url!, options: [:], completionHandler: nil)
                }
            }
            return false
        }
        
    }
    @IBAction func goback(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let newViewController = storyBoard.instantiateViewController(withIdentifier: "Accueil") as! UINavigationController
        self.present(newViewController, animated: true, completion: nil)

    }
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        
        SweetAlert().showAlert("Are you sure?", subTitle: "The saved place will permanently delete!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, delete it!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
            if isOtherButton == true {
                
              _ =  SweetAlert().showAlert("Cancelled!", subTitle: "saved is safe", style: AlertStyle.error)
            }
            else {
                let ii = self.feedItems[indexPath.item]
                let ff = String(describing: ii["id"] as! Int)
                self.feedItems.remove(at: indexPath.item)
                self.tableview.deleteRows(at: [indexPath], with: .automatic)
                
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                
                
                let urlPath = URL(string: NavigatorData.URL+"/api/Deletemysaved?id="+ff)
                print(ff)
                Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
                _ = SweetAlert().showAlert("Deleted!", subTitle: "saved place has been deleted!", style: AlertStyle.success)
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
}
