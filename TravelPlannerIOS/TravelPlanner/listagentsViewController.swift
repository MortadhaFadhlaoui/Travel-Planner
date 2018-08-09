//
//  listagentsViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 09/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CRRefresh
import CoreData
class listagentsViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
    var feedItems = [Dictionary<String, AnyObject>]()
    var people:[NSManagedObject] = []
    
    
    @IBOutlet weak var tableview: UITableView!
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    
    override func viewWillAppear(_ animated: Bool) {
       
        
        if Connectivity.isConnectedToInternet() == true {
             load.startAnimating()
            
            feedItems.removeAll()
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getallagents")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["agents"] as? NSArray
                    
                    for places in (dictp1! as NSArray) {
                        let place = places as? Dictionary<String, AnyObject>
                        self.feedItems.append(place!)
                        
                    }
                    self.load.isHidden = true
                    
                    if(self.feedItems.count == 0){
                       _ = SweetAlert().showAlert("Error!", subTitle: "You have no agents!", style: AlertStyle.error)
                    }
                    self.tableview.reloadData()
                    
                    
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
            /// start refresh
            /// Do anything you want...
            self?.feedItems.removeAll()
            
            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
            let urlPath = URL(string: NavigatorData.URL+"/api/Getallagents")
            
            Alamofire.request(urlPath!,method:.post,headers: Auth_header).responseJSON { responsep in
                if(responsep.result.isSuccess){
                    // let userm = UserModel()
                    
                    let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                    let dictp1 = dictp?["agents"] as? NSArray
                    
                    for places in (dictp1! as NSArray) {
                        let place = places as? Dictionary<String, AnyObject>
                        self?.feedItems.append(place!)
                        
                    }
                    
                    if(self?.feedItems.count == 0){
                        _ = SweetAlert().showAlert("Error!", subTitle: "You have no agents!", style: AlertStyle.error)
                    }
                    self?.tableview.reloadData()
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
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "myagentcell", for: indexPath)
        
       let nearby = self.feedItems[indexPath.row]
            as Dictionary<String, AnyObject>
        
        print(nearby)
        let lblName:UILabel = cell.viewWithTag(2) as! UILabel
        lblName.text = nearby["email"] as! String?
        let lblcategorie:UILabel = cell.viewWithTag(3) as! UILabel
        lblcategorie.text = nearby["username"] as! String?
        
        let lblctext:UILabel = cell.viewWithTag(4) as! UILabel
        let x = nearby["numtel"] as! NSNumber?
        lblctext.text = String(describing: x!)
        
       
        
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
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        
        SweetAlert().showAlert("Are you sure?", subTitle: "The agent will permanently delete!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, delete it!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
            if isOtherButton == true {
             
                _ = SweetAlert().showAlert("Cancelled!", subTitle: "Agent is safe", style: AlertStyle.error)
            }
            else {
                let ii = self.feedItems[indexPath.item]
                let ff = String(describing: ii["id"] as! Int)
                self.self.feedItems.remove(at: indexPath.item)
                self.tableview.deleteRows(at: [indexPath], with: .automatic)
                
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                
                
                let urlPath = URL(string: NavigatorData.URL+"/api/Deletemyagent?id="+ff)
                print(ff)
                Alamofire.request(urlPath!,method:.get,headers: Auth_header).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
               _ = SweetAlert().showAlert("Deleted!", subTitle: "Agent has been deleted!", style: AlertStyle.success)
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

