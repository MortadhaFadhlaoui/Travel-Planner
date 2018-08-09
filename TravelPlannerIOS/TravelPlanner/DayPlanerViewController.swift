//
//  DayPlanerViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 14/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire

class DayPlanerViewController: UIViewController, UITableViewDelegate, UITableViewDataSource  {
   var payspack = PaysModel()
    var refreshControl: UIRefreshControl!
    var dayplansitem : [DayPanModel]  = []
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        refreshControl = UIRefreshControl()
        refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        refreshControl!.addTarget(self, action: #selector(NearbyThingsViewController.refresh(sender:)), for: UIControlEvents.valueChanged)
        
        tableView.addSubview(refreshControl) // not required when using UITableViewController
        
        loadData()
        
        }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

   func loadData() -> Void {
    if Connectivity.isConnectedToInternet() {
    tableView.estimatedRowHeight = 100.0
    tableView.rowHeight = UITableViewAutomaticDimension
    let urlPath = URL(string: NavigatorData.URL+"/api/getdayplanmorta/\((payspack.Pack!.idPack)!)")
    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
    //let parameters = [:] as [String : Any]
    
    Alamofire.request(urlPath!, method: .post,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
        if(responsep.result.isSuccess){
            let dict = responsep.result.value as? Dictionary<String, AnyObject>
            let success = dict?["success"]! as! NSNumber
            
            if(success==1){  
            let daysplan = dict?["dayplans"] as? NSArray
            self.dayplansitem.removeAll()
            for items in (daysplan! as NSArray) {
                let item = items as? Dictionary<String, AnyObject>
                let idDay = item!["id"] as! Int
                let title = item!["title"] as! String
                let date = item!["date"] as! String
                let description = item!["description"] as! String
                let date_d = date.substring(to:date.index(date.startIndex, offsetBy: 10))
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd" //Your date format
                dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
                
                let date_db = dateFormatter.date(from: date_d) //according to date
                
                let daypla = DayPanModel()
                daypla.idDay = idDay as Int?
                daypla.title = title
                daypla.descriptionday = description
                daypla.date = date_db
                
                
                self.dayplansitem.append(daypla)
                self.tableView.reloadData()
                }
                
            }
            
        }else if(responsep.result.isFailure){
            print("le le")
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
                
            }
        }
        
    }
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        var numOfSections: Int = 0
        if dayplansitem.count != 0
        {
            self.tableView.separatorStyle = .singleLine
            numOfSections            = 1
            self.tableView.backgroundView = nil
        }
        else
        {
            tableView.isHidden = false
            let noDataLabel: UILabel     = UILabel(frame: CGRect(x: 0, y: 0, width: self.tableView.bounds.size.width, height: self.tableView.bounds.size.height))
            noDataLabel.text          = "No day plans for this pack"
            noDataLabel.textColor     = UIColor.black
            noDataLabel.textAlignment = .center
            self.tableView.backgroundView  = noDataLabel
            self.tableView.separatorStyle  = .none
        }
        return numOfSections
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       
        return dayplansitem.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let dayplanitem: DayPanModel!
       
            dayplanitem = dayplansitem[indexPath.row]

        
        let lblArrive:UILabel = cell.viewWithTag(1) as! UILabel
        lblArrive.text = dayplanitem.title
        
        let lblDescription:UILabel = cell.viewWithTag(3) as! UILabel
        lblDescription.text = dayplanitem.descriptionday
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        let date_f = formatter.string(from: (dayplanitem.date)!).substring(to:formatter.string(from: (dayplanitem.date)!).index(formatter.string(from: (dayplanitem.date)!).startIndex, offsetBy: 10))
        let lblDateDepart:UILabel = cell.viewWithTag(2) as! UILabel
        
        let imageAttachment =  NSTextAttachment()
        imageAttachment.image = #imageLiteral(resourceName: "clockmortaa")
        //Set bound to reposition
        let imageOffsetY:CGFloat = -5.0;
        imageAttachment.bounds = CGRect(x: 0, y: imageOffsetY, width: imageAttachment.image!.size.width, height: imageAttachment.image!.size.height)
        //Create string with attachment
        let attachmentString = NSAttributedString(attachment: imageAttachment)
        //Initialize mutable string
        let completeText = NSMutableAttributedString(string: "")
        //Add image to mutable string
        completeText.append(attachmentString)
        //Add your text to mutable string
        let  textAfterIcon = NSMutableAttributedString(string: " "+date_f)
        completeText.append(textAfterIcon)
        lblDateDepart.textAlignment = .center;
        lblDateDepart.attributedText = completeText
        return cell
    }
 
    
    
    @IBAction func reserve(_ sender: Any) {
        
    }
    func refresh(sender:AnyObject) {
        loadData()
        refreshControl.endRefreshing()
        tableView.reloadData()
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
