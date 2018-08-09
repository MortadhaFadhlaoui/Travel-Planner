//
//  NearbyThingsViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 21/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import CoreLocation
import MapKit
import Alamofire
import SDWebImage

class NearbyThingsViewController: UIViewController, CLLocationManagerDelegate, UITableViewDelegate, UITableViewDataSource,UISearchBarDelegate {
    
    //1TMLITAW5QDEZHORCK50IWNYEDHYN0IR4RKZE4KEB4IRVW1R
    //XBYYKCS5AJKDVZIEIDH13WZBWNI5YPJA2HRGBSB41ETVZEHI
    
    //TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV
    //0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG
    
    //CXWUEH3AFTRRANJU0WHYPJ0JT0J4QOXVEC0TKIH1GOVYHYNP
    //FSLMVUBMNRVT0CNPVW2RX0IXXUAL3IXLYESA50GYRQ45OBVD
    //36,898033
    //10,189533
    //@IBOutlet weak var map: MKMapView!
    @IBOutlet weak var search: UISearchBar!
    
    var refreshControl: UIRefreshControl!
    
    @IBOutlet weak var filter: UISegmentedControl!
    @IBOutlet weak var load: UIActivityIndicatorView!
    var feedItems : [NearbyModel]  = []
    var feedItems1 : [NearbyModel]  = []
    var type : String = ""
    var details: DetailsNearbyViewController?
    @IBOutlet weak var tableView: UITableView!
    var locationManager: CLLocationManager!
    var filteredData : [NearbyModel] = []
    var filteredData1 : [NearbyModel] = []
    var searchedData : [NearbyModel] = []
    var isSearching = false
    var isFiltring = false
    let when = DispatchTime.now() + 1 // change 2 to desired number of seconds
    override func viewDidLoad() {
        super.viewDidLoad()
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl!.addTarget(self, action: #selector(NearbyThingsViewController.refresh(sender:)), for: UIControlEvents.valueChanged)
        
        tableView.addSubview(refreshControl) // not required when using UITableViewController
        
        loadData()
    }
    func loadData() -> Void {
        
        if Connectivity.isConnectedToInternet() {
            self.load.isHidden = false
            self.load.startAnimating()
            search.delegate = self
            search.returnKeyType = UIReturnKeyType.done
            locationManager = CLLocationManager()
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBest
            
            if Reachability.isLocationServiceEnabled() == true {
                locationManager.requestAlwaysAuthorization()
                locationManager.startUpdatingLocation()
                locationManager.requestWhenInUseAuthorization()
                let date = Date()
                let formatter = DateFormatter()
                formatter.dateFormat = "yyyyMMdd"
                let result = formatter.string(from: date)
                
                let url = URL(string: "https://api.foursquare.com/v2/venues/explore?ll=\(locationManager.location!.coordinate.latitude),\(locationManager.location!.coordinate.longitude)&client_id=TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV&client_secret=0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG&v=\(result)")!
                Alamofire.request(url,method:.get).responseJSON { response in
                    if(response.result.isSuccess){
                        self.feedItems = []
                        self.filteredData = []
                        self.filteredData1 = []
                        self.feedItems1 = []
                        let dict = response.result.value as? Dictionary<String, AnyObject>
                        
                        let responsee = dict?["response"] as? Dictionary<String, AnyObject>
                        let groups = responsee?["groups"] as? NSArray
                        let groupe = groups?[0] as? Dictionary<String, AnyObject>
                        let items = groupe!["items"] as? NSArray
                        for places in (items! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            let venue = place?["venue"] as? Dictionary<String, AnyObject>
                            let name = venue!["name"] as! String
                            let id = venue!["id"] as! String
                            let urlphoto = URL(string: "https://api.foursquare.com/v2/venues/\(id)/photos?client_id=TMUWUAED1DRG0VIIUQJWQ3X1XQ4TA3JJDUZBIINXRVNKEOPV&client_secret=0AHQCKHLHFSU15JEYOUKW1EBLGMGM02PRTWZ0HA2MT1GH5LG&v=\(result)")!
                            let nearby = NearbyModel()
                            Alamofire.request(urlphoto,method:.get).responseJSON { responsep in
                                if(responsep.result.isSuccess){
                                    let dictp = responsep.result.value as? Dictionary<String, AnyObject>
                                    let responsephoto = dictp?["response"] as? Dictionary<String, AnyObject>
                                    let photos = responsephoto?["photos"] as? Dictionary<String, AnyObject>
                                    let itemsp = photos?["items"] as? NSArray
                                    
                                    if (itemsp?.count != 0) {
                                        let itemp = itemsp?[0] as? Dictionary<String, AnyObject>
                                        
                                        let prefixp = itemp!["prefix"] as! String
                                        let suffixp = itemp!["suffix"] as! String
                                        
                                        let imagep = prefixp + "300x300" + suffixp
                                        nearby.image = imagep
                                        
                                    }
                                    self.tableView.reloadData()
                                    
                                }else if(responsep.result.isFailure){
                                    print("le le")
                                }
                            }
                            
                            let location = venue!["location"] as? Dictionary<String, AnyObject>
                            let lat = location!["lat"] as! Double
                            let lng = location!["lng"] as! Double
                            let distance = location!["distance"] as? Int
                            
                            if(venue!["rating"] != nil){
                                let rating = venue!["rating"] as! Double
                                let ratingColor = venue!["ratingColor"] as! String
                                nearby.rating = rating
                                nearby.ratingColor = ratingColor
                            }
                            
                            let hours = venue!["hours"] as? Dictionary<String, AnyObject>
                            
                            if hours != nil {
                                let isOpen = hours!["isOpen"] as? Bool
                                nearby.isOpen = isOpen
                            }
                            let categories = venue?["categories"] as? NSArray
                            let categorie = categories?[0] as? Dictionary<String, AnyObject>
                            let categoriename = categorie!["name"] as! String
                            let icon = categorie!["icon"] as? Dictionary<String, AnyObject>
                            let prefix = icon!["prefix"] as! String
                            let suffix = icon!["suffix"] as! String
                            let image = prefix + "88" + suffix
                            let tips = place?["tips"] as? NSArray
                            
                            nearby.name = name
                            nearby.id = id
                            nearby.categorie = categoriename
                            nearby.lat = lat
                            nearby.lng = lng
                            nearby.distance = distance
                            
                            if tips != nil
                            {
                                let tip = tips?[0] as? Dictionary<String, AnyObject>
                                let text = tip!["text"] as! String
                                nearby.tips = text
                                
                            }
                            nearby.iconcat = image
                            self.feedItems.append(nearby)
                            self.filteredData.append(nearby)
                            self.filteredData1.append(nearby)
                            self.feedItems1.append(nearby)
                            self.tableView.reloadData()
                            self.load.isHidden = true
                            self.tableView.isHidden = false
                        }
                        
                    }else if(response.result.isFailure){
                        print("le le")
                    }
                }
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
                        }
                        
                    }
                }
            }
            
            
        }else{
            self.load.isHidden = false
            self.load.startAnimating()
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
                    
                }
            }
        }
        
        
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        var numOfSections: Int = 0
        if filteredData.count != 0
        {
            self.tableView.separatorStyle = .singleLine
            numOfSections   = 1
            self.tableView.backgroundView = nil
        }
        else
        {
            tableView.isHidden = false
            load.isHidden = true
            let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: self.tableView.bounds.size.width, height: self.tableView.bounds.size.height))
            noDataLabel.text          = "No data available"
            noDataLabel.textColor     = UIColor.black
            noDataLabel.textAlignment = .center
            self.tableView.backgroundView  = noDataLabel
            self.tableView.separatorStyle  = .none
        }
        return numOfSections
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if isSearching && isFiltring{
            return filteredData1.count
        }else if isSearching {
            return searchedData.count
        }else{
            return filteredData.count
        }
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "myCell", for: indexPath)
        let nearby: NearbyModel!
        if isSearching && isFiltring{
            nearby = self.filteredData1[indexPath.row]
        }else if isSearching{
            nearby = self.searchedData[indexPath.row]
        }else{
            nearby = self.filteredData[indexPath.row]
        }
        
        //cell.textLabel?.text = "Hello from new Cell"
        
        let lblName:UILabel = cell.viewWithTag(2) as! UILabel
        lblName.text = nearby?.name
        let lblcategorie:UILabel = cell.viewWithTag(3) as! UILabel
        lblcategorie.text = nearby?.categorie
        
        let lblctext:UILabel = cell.viewWithTag(4) as! UILabel
        lblctext.text = nearby?.tips
        let a: String
        if nearby?.image == nil {
            a = (nearby?.iconcat)! as String
        }else{
            a = (nearby?.image)! as String
        }
        /*let url = NSURL(string: a)
         let data = NSData(contentsOf:url! as URL)*/
        let imageURL:UIImageView = cell.viewWithTag(1) as! UIImageView
        imageURL.layer.borderWidth = 1
        imageURL.layer.masksToBounds = false
        
        imageURL.layer.cornerRadius = imageURL.frame.height/4
        imageURL.clipsToBounds = true
        
        imageURL.layer.borderColor = UIColor.white.cgColor
        
        
        //imageURL.image = UIImage(data: data! as Data)
        imageURL.sd_setImage(with: URL(string: a), placeholderImage: UIImage(named: "placeholder.png"))
        
        
        
        return cell
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "detailsShow" {
            let index : Int = (tableView.indexPathForSelectedRow?.row)!
            //let personne =  self.personnes[index]
            self.details = segue.destination as? DetailsNearbyViewController
            if isSearching && isFiltring{
                self.details?.nearby = filteredData1[index]
            }else if isSearching{
                self.details?.nearby = searchedData[index]
            }else{
                self.details?.nearby = filteredData[index]
            }
        }
        
    }
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        isFiltring = false
        if type == "open" {
            filteredData = []
            for item in feedItems {
                if item.isOpen?.description == "true" {
                    filteredData.append(item)
                }
            }
            
        }
        if type == "rating" {
            filteredData = []
            feedItems.sort(by: {$0.rating! > $1.rating!})
            filteredData = feedItems
        }
        if type == "all" {
            filteredData = []
            filteredData = feedItems1
        }
        if type == "disance" {
            filteredData = []
            feedItems.sort(by: {$0.distance! < $1.distance!})
            filteredData = feedItems
            
        }
        
        if search.text == nil || search.text == "" {
            isSearching = false
            view.endEditing(true)
            self.tableView.reloadData()
        }else{
            searchedData = []
            isSearching = true
            for item in filteredData {
                if ((item.name!.lowercased().contains((search.text!).lowercased())) || (item.categorie!.lowercased().contains((search.text!).lowercased())) ){
                    searchedData.append(item)
                }
            }
            self.tableView.reloadData()
        }
    }
    
    
    
    @IBAction func filteraction(_ sender: Any) {
        switch self.filter.selectedSegmentIndex {
        case 0:
            type = "all"
            break
        case 1:
            type = "disance"
            break
        case 2:
            type = "rating"
            break
        case 3:
            type = "open"
            break
        default:
            print("ok")
        }
        if isSearching{
            isFiltring = true
            if type == "open" {
                self.load.isHidden = false
                self.load.startAnimating()
                self.tableView.isHidden = true
                filteredData1 = []
                for item in searchedData {
                    if item.isOpen?.description == "true" {
                        filteredData1.append(item)
                    }
                }
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
                
            }
            if type == "rating" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData1 = []
                searchedData.sort(by: {$0.rating! > $1.rating!})
                filteredData1 = searchedData
                
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
            }
            if type == "all" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData1 = []
                filteredData1 = searchedData
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
            }
            if type == "disance" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData1 = []
                searchedData.sort(by: {$0.distance! < $1.distance!})
                filteredData1 = searchedData
                
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
            }
            
        }else{
            isFiltring = false
            if type == "open" {
                self.load.isHidden = false
                self.load.startAnimating()
                self.tableView.isHidden = true
                filteredData = []
                for item in feedItems {
                    if item.isOpen?.description == "true" {
                        filteredData.append(item)
                    }
                }
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
                
            }
            if type == "rating" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData = []
                feedItems.sort(by: {$0.rating! > $1.rating!})
                filteredData = feedItems
                
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
            }
            if type == "all" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData = []
                filteredData = feedItems1
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
                }
            }
            if type == "disance" {
                self.tableView.isHidden = true
                self.load.isHidden = false
                self.load.startAnimating()
                filteredData = []
                feedItems.sort(by: {$0.distance! < $1.distance!})
                filteredData = feedItems
                
                DispatchQueue.main.asyncAfter(deadline: when) {
                    self.tableView.reloadData()
                    self.tableView.isHidden = false
                    self.load.isHidden = true
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
    
    func refresh(sender:AnyObject) {
        loadData()
        self.filter.selectedSegmentIndex = 0
        refreshControl.endRefreshing()
        
    }
}

