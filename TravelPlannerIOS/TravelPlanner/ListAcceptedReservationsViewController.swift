//
//  ListAcceptedReservationsViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 09/01/2018.
//  Copyright © 2018 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
import CoreData
class ListAcceptedReservationsViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
var people:[NSManagedObject] = []
    var feedItems = [Dictionary<String, AnyObject>]()
    
    
    @IBOutlet weak var loader: UIActivityIndicatorView!
    @IBOutlet weak var listview: UITableView!
    var paysss = [NSArray]()
    var packs = [NSArray]()
    override func viewWillAppear(_ animated: Bool) {
       
        
        if Connectivity.isConnectedToInternet() == true {
             loader.startAnimating()
            feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getacceptedres")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["res"] as? NSArray
                    
                    
                    if(dictp1 != nil){
                        
                        
                        
                        for places in (dictp1! as NSArray) {
                            let place = places as? Dictionary<String, AnyObject>
                            self.feedItems.append(place!)
                            
                        }
                        
                        let dictp00 = dictp?["users"] as? NSArray
                        for placesff in (dictp00! as NSArray) {
                            let place0 = placesff as? NSArray
                            self.paysss.append(place0!)
                        }
                        
                        let dictp000 = dictp?["packs"] as? NSArray
                        
                        for placesfff in (dictp000! as NSArray) {
                            let place00 = placesfff as? NSArray
                            self.packs.append(place00!)
                            self.listview.reloadData()
                            
                        }
                        
                    }
                    
                    if(self.feedItems.count == 0){
                     _ =   SweetAlert().showAlert("Error!", subTitle: "You have no accepted reservations!", style: AlertStyle.error)
                    }
                    self.loader.isHidden = true
                    
                    
                    
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
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        listview.cr.addHeadRefresh(animator: FastAnimator()) { [weak self] in
            self?.feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getacceptedres")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["res"] as? NSArray
                    
                    if(dictp1 != nil){
                        
                    
                    
                    for places in (dictp1! as NSArray) {
                        let place = places as? Dictionary<String, AnyObject>
                        self?.feedItems.append(place!)
                        
                    }
                    
                    let dictp00 = dictp?["users"] as? NSArray
                    
                    
                    
                    for placesff in (dictp00! as NSArray) {
                        let place0 = placesff as? NSArray
                        
                        
                        
                        self?.paysss.append(place0!)
                        
                        
                    }
                    
                    
                    let dictp000 = dictp?["packs"] as? NSArray
                    
                    
                    
                    for placesfff in (dictp000! as NSArray) {
                        let place00 = placesfff as? NSArray
                        
                        
                        
                        self?.packs.append(place00!)
                        self?.listview.reloadData()
                        
                    }
                    
                    }
                    if(self?.feedItems.count == 0){
                      _ =  SweetAlert().showAlert("Error!", subTitle: "You have no accepted reservations!", style: AlertStyle.error)
                    }
                    
                    
                    
                    self?.listview.layoutIfNeeded()
                    self?.listview.beginUpdates()
                    self?.listview.endUpdates()
                    self?.listview.cr.endHeaderRefresh()
                    
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
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "reservationsaccepted", for: indexPath)
        
        let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        
        let lblName:UILabel = cell.viewWithTag(1) as! UILabel
        let hh = nearby["date"] as! String?
        let xy1 = hh?.substring(to:(hh?.index((hh?.startIndex)!, offsetBy: 10))!)
        lblName.text = xy1
        
        
        let nearby00 = self.paysss[indexPath.row]
            as NSArray
        let ff1 = nearby00[0] as? Dictionary<String, AnyObject>
        
        
        
        let lblcategorie:UILabel = cell.viewWithTag(2) as! UILabel
        let xxx1 = ff1?["username"] as! String?
        let xxx2 = ff1?["email"] as! String?
        lblcategorie.text = xxx1!+"=>"+xxx2!
        
        
        let nearby000 = self.packs[indexPath.row]
            as NSArray
        let fff1 = nearby000[0] as? Dictionary<String, AnyObject>
        
        
        
        let lblctext:UILabel = cell.viewWithTag(3) as! UILabel
        let xxxx1 = fff1?["nom_depart"] as! String?
        let xxxx2 = fff1?["prix"] as! NSNumber?
        let xxxxx2 = String(describing: xxxx2!)
        lblctext.text = xxxx1!+"=>"+xxxxx2+" Dt"
        
        
        
        return cell
        
        
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
   
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }

   
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let delete = UITableViewRowAction(style: .destructive, title: "Delete") { (action, indexPath) in
            
            
            SweetAlert().showAlert("Are you sure?", subTitle: "The reservation will permanently delete!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:self.UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, delete it!", otherButtonColor: self.UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                  _ =  SweetAlert().showAlert("Cancelled!", subTitle: "reservation is safe", style: AlertStyle.error)
                }
                else {
                    let ii = self.feedItems[indexPath.item]
                    let ff = String(describing: ii["id"] as! Int)
                    print(ff)
                    self.feedItems.remove(at: indexPath.item)
                    self.paysss.remove(at: indexPath.item)
                    self.packs.remove(at: indexPath.item)
                    self.listview.deleteRows(at: [indexPath], with: .automatic)
                    
                    let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                    
                    
                    let urlPath = URL(string: NavigatorData.URL+"/api/deletereservation?id="+ff)
                    print(ff)
                    Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                        if(responsep.result.isSuccess){
                            
                            
                            
                        }else if(responsep.result.isFailure){
                            print("le le")
                        }
                        
                    }
                   _ = SweetAlert().showAlert("Deleted!", subTitle: "reservation has been deleted!", style: AlertStyle.success)
                }
            }
            
            
            
        }
        
        let share = UITableViewRowAction(style: .normal, title: "call") { (action, indexPath) in
            let nearby00 = self.paysss[indexPath.row]
                as NSArray
            let ff1 = nearby00[0] as? Dictionary<String, AnyObject>
            let xxxx2 = ff1?["numtel"] as! NSNumber?
            let xxxxx2 = String(describing: xxxx2!)
            let url: NSURL = URL(string: "TEL://" + xxxxx2)! as NSURL
            
            UIApplication.shared.open(url as URL, options: [:], completionHandler: nil)
        }
        
        share.backgroundColor = UIColor.seagreen
        
        return [delete, share]
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
