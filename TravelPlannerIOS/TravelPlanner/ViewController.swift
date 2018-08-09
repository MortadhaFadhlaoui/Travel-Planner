//
//  ViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 08/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CoreData
class ViewController: UIViewController{
    var people:[NSManagedObject] = []
    var feedItems: NSArray = NSArray()
    var connecteduser: UserModel = UserModel()
    var ok: UINavigationController?
    @IBOutlet weak var pwdtxt: UITextField!

    @IBOutlet weak var logintxt: UITextField!
    @IBOutlet weak var loginbtn: UIButton!
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //1
        guard let appDelegate =
            UIApplication.shared.delegate as? AppDelegate else {
                return
        }
        
        let managedContext =
            appDelegate.persistentContainer.viewContext
        
        //2
        let fetchRequest =
            NSFetchRequest<NSManagedObject>(entityName: "User")
        //3
        do {
            people = try managedContext.fetch(fetchRequest)
        } catch let error as NSError {
            print("Could not fetch. \(error), \(error.userInfo)")
        }
        print(people.count)
        if(people.count != 0){
            logintxt.text = people[0].value(forKey: "username")! as? String
            pwdtxt.text = people[0].value(forKey: "password")! as? String
        }
        
        
        
        let color = UIColor.white
        logintxt.attributedPlaceholder = NSAttributedString(string: logintxt.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        pwdtxt.attributedPlaceholder = NSAttributedString(string: pwdtxt.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        
        
        loginbtn.layer.cornerRadius = 13
        logintxt.layer.cornerRadius = 13
        pwdtxt.layer.cornerRadius = 13
        logintxt.leftViewMode = UITextFieldViewMode.always
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let image = UIImage(named: "log.png")
        
        imageView.image = image
        logintxt.leftView = imageView
        
        pwdtxt.leftViewMode = UITextFieldViewMode.always
        
        let imageViewp = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagep = UIImage(named: "pwd.png")
        imageViewp.image = imagep
        pwdtxt.leftView = imageViewp
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func loginAction(_ sender: Any) {
        let trimmedString = logintxt.text!.trimmingCharacters(in: .whitespaces)
        let urlPath = URL(string: NavigatorData.URL+"/api/login?username="+trimmedString+"&password="+pwdtxt.text!)
        Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
            if(responsep.result.isSuccess){
               // let userm = UserModel()
                
                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
           

                let success = Int((dictp?["success"])! as! NSNumber)
             
                if(success==1){
                    
                    
                    
                    
                    
                    
                    
                    
                    //let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                    let data = dictp?["data"]! as? Dictionary<String, AnyObject>
                    let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                    
                    
                    
                    
               
                    let image = user?["image"] as? String
                    let nom = user?["nom"] as? String
                    let prenom = user?["prenom"] as? String
                    let username = user?["username"] as? String
                    let role = user?["role"] as? String
                    let numtel = user?["numtel"]! as? Int
                    let email = user?["email"] as? String
                    let id = user?["id"]! as? Int
                    SignUpViewController.connecteduser.idUser = id
                    SignUpViewController.connecteduser.nom = nom
                    SignUpViewController.connecteduser.prenom = prenom
                    SignUpViewController.connecteduser.username = username
                    SignUpViewController.connecteduser.role = role
                    SignUpViewController.connecteduser.email = email
                    SignUpViewController.connecteduser.numtel = String(describing: numtel!)
                    SignUpViewController.connecteduser.image = image
                    
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
                    self.save(name: username!, pwd: self.pwdtxt.text!)
                    SignUpViewController.tokensec = data?["token"] as! String
                    if(user?["role"] as? String == "traveler"){
                        
                        
                        
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "Accueil") as! UINavigationController
                    self.present(newViewController, animated: true, completion: nil)
                        
                        
                        
                    }else{
                        
                        let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                        let urlPath1 = URL(string: NavigatorData.URL+"/api/getagence")
                        Alamofire.request(urlPath1!,method:.post,headers: Auth_header).responseJSON { responsep in
                            if(responsep.result.isSuccess){
                        let dictp1 = responsep.result.value! as? Dictionary<String, AnyObject>
                      
                               
                                
                                if(dictp1?["photo"] as? String == nil){
                                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "addagence") 	
                                    self.present(newViewController, animated: true, completion: nil)
                                }else{
                                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthome") as! UITabBarController
                                    self.present(newViewController, animated: true, completion: nil)
                                }
                        
                        
                            }
                        }
                    }
                }else{
                    self.showToast(message: "check credentials")
                    
                }
             
                
                
                
            }else if(responsep.result.isFailure){
                print("le le")
            }
            
        }
        
    }
  
    /*  func itemsDownloaded(items: NSArray) {
        
        feedItems = items
        if (items.count==0){
            showToast(message: "incorrect credentials")
        }else{
            let user = items[0] as? UserModel
            if (user?.role == "traveler") {
                let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let newViewController = storyBoard.instantiateViewController(withIdentifier: "Accueil") as! UINavigationController
                self.present(newViewController, animated: true, completion: nil)

            }else
            {
                let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let newViewController = storyBoard.instantiateViewController(withIdentifier: "company") as! UITabBarController
                self.present(newViewController, animated: true, completion: nil)

            }
            
        }
    }*/
   
    func showToast(message : String) {
        
        let toastLabel = UILabel(frame: CGRect(x: self.view.frame.size.width/2 - 95, y: self.view.frame.size.height-100, width: 170, height: 35))
        toastLabel.backgroundColor = UIColor.black.withAlphaComponent(0.6)
        toastLabel.textColor = UIColor.white
        toastLabel.textAlignment = .center;
        toastLabel.font = UIFont(name: "Montserrat-Light", size: 12.0)
        toastLabel.text = message
        toastLabel.alpha = 1.0
        toastLabel.layer.cornerRadius = 10;
        toastLabel.clipsToBounds  =  true
        self.view.addSubview(toastLabel)
        UIView.animate(withDuration: 4.0, delay: 0.1, options: .curveEaseOut, animations: {
            toastLabel.alpha = 0.0
        }, completion: {(isCompleted) in
            toastLabel.removeFromSuperview()
        })
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


