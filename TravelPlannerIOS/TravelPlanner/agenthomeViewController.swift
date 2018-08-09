//
//  agenthomeViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 06/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
import CoreData
class agenthomeViewController: UIViewController {
var people:[NSManagedObject] = []
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var first: UILabel!
    @IBOutlet weak var second: UILabel!
    @IBOutlet weak var third: UILabel!
    @IBOutlet weak var forth: UILabel!
    @IBOutlet weak var fifth: UILabel!
  
    @IBOutlet weak var green: UIImageView!
    @IBOutlet weak var bg: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()
        if Connectivity.isConnectedToInternet() == true {
            
        
        green.isHidden = true
        img.isHidden = true
        img.layer.borderWidth = 1
        img.layer.masksToBounds = false
    
        img.layer.cornerRadius = img.frame.height/2
        img.clipsToBounds = true
        
        img.layer.borderColor = UIColor.white.cgColor
        
        
        print(SignUpViewController.tokensec)
        load.startAnimating()
        bg.isHidden = true
        let urlPath = URL(string: NavigatorData.URL+"/api/getagence")
        
       
        let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
        Alamofire.request(urlPath!, method: .post,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
            if(responsep.result.isSuccess){
                
                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                let dic1 = dictp?["photo"] as? String
                if let decodedData = Data(base64Encoded: dic1!, options: .ignoreUnknownCharacters) {
                    let image = UIImage(data: decodedData)
                    self.img.image = image
                    ModifMyAgencyViewController.imgm = image!
                }
                let dict2 = dictp?["agences"] as? NSArray
                let dict3 = dict2?[0] as? Dictionary<String, AnyObject>
                
               
                //Set bound to reposition
                let imageOffsetYm:CGFloat = -5.0;
             
                //Initialize mutable string
                let completeTextm = NSMutableAttributedString(string: "")
                //Add image to mutable string
              
                //Add your text to mutable string
                let  textAfterIconm = NSMutableAttributedString(string: dict3?["nom"] as! String )
                let nn1 = dict3?["nom"] as! String
                ModifMyAgencyViewController.nomm = nn1
                completeTextm.append(textAfterIconm)
                
                self.first.attributedText = completeTextm;
                
                let myid = dict3?["id"] as! NSInteger
                let idd = String(myid)
                ModifMyAgencyViewController.idag = idd
                
                let imageAttachmentm1 =  NSTextAttachment()
                imageAttachmentm1.image = UIImage(named:"envelope")
                //Set bound to reposition
                
                imageAttachmentm1.bounds = CGRect(x: 0, y: imageOffsetYm, width: imageAttachmentm1.image!.size.width, height: imageAttachmentm1.image!.size.height)
                //Create string with attachment
                let attachmentStringm1 = NSAttributedString(attachment: imageAttachmentm1)
                //Initialize mutable string
                let completeTextm1 = NSMutableAttributedString(string: "")
                //Add image to mutable string
                completeTextm1.append(attachmentStringm1)
                //Add your text to mutable string
                let y1 = dict3?["email"] as! String
                
                let nn2 = dict3?["email"] as! String
                ModifMyAgencyViewController.mailm = nn2
                let  textAfterIconm1 = NSMutableAttributedString(string: "   "+y1)
                completeTextm1.append(textAfterIconm1)
                
                self.second.attributedText = completeTextm1;
                
                
                
                let imageAttachmentm2 =  NSTextAttachment()
                imageAttachmentm2.image = UIImage(named:"address")
                //Set bound to reposition
                
                imageAttachmentm2.bounds = CGRect(x: 0, y: imageOffsetYm, width: imageAttachmentm2.image!.size.width, height: imageAttachmentm2.image!.size.height)
                //Create string with attachment
                let attachmentStringm2 = NSAttributedString(attachment: imageAttachmentm2)
                //Initialize mutable string
                let completeTextm2 = NSMutableAttributedString(string: "")
                //Add image to mutable string
                completeTextm2.append(attachmentStringm2)
                //Add your text to mutable string
                let y2 = dict3?["adresse"] as! String
                let nn3 = dict3?["adresse"] as! String
                ModifMyAgencyViewController.addressm = nn3
                let  textAfterIconm2 = NSMutableAttributedString(string: "   "+y2)
                completeTextm2.append(textAfterIconm2)
                
                self.third.attributedText = completeTextm2;
                
                
                
                
                let imageAttachmentm3 =  NSTextAttachment()
                imageAttachmentm3.image = UIImage(named:"phone")
                //Set bound to reposition
                
                imageAttachmentm3.bounds = CGRect(x: 0, y: imageOffsetYm, width: imageAttachmentm3.image!.size.width, height: imageAttachmentm3.image!.size.height)
                //Create string with attachment
                let attachmentStringm3 = NSAttributedString(attachment: imageAttachmentm3)
                //Initialize mutable string
                let completeTextm3 = NSMutableAttributedString(string: "")
                //Add image to mutable string
                completeTextm3.append(attachmentStringm3)
                //Add your text to mutable string
                let y3 = dict3?["num_tel"] as! String
                let nn4 = dict3?["num_tel"] as! String
                ModifMyAgencyViewController.numtelm = nn4
                let  textAfterIconm3 = NSMutableAttributedString(string: "   "+y3)
                completeTextm3.append(textAfterIconm3)
                
                self.forth.attributedText = completeTextm3;
                
                
                
                let imageAttachmentm4 =  NSTextAttachment()
                imageAttachmentm4.image = UIImage(named:"faxx")
                //Set bound to reposition
                
                imageAttachmentm4.bounds = CGRect(x: 0, y: imageOffsetYm, width: imageAttachmentm4.image!.size.width, height: imageAttachmentm4.image!.size.height)
                //Create string with attachment
                let attachmentStringm4 = NSAttributedString(attachment: imageAttachmentm4)
                //Initialize mutable string
                let completeTextm4 = NSMutableAttributedString(string: "")
                //Add image to mutable string
                completeTextm4.append(attachmentStringm4)
                //Add your text to mutable string
                if(dict3?["num_fax"] as? String == nil){
                    ModifMyAgencyViewController.faxm = "Fax not set yet"
                    let  textAfterIconm4 = NSMutableAttributedString(string: "   Fax not set yet")
                    completeTextm4.append(textAfterIconm4)
                    
                }else{
                    let nn5 = dict3?["num_fax"] as! String
                    ModifMyAgencyViewController.faxm = nn5
                    let y4 = dict3?["num_fax"] as! String
                    let  textAfterIconm4 = NSMutableAttributedString(string: "   "+y4)
                    completeTextm4.append(textAfterIconm4)
                }
                
                self.green.isHidden = false
                
                self.fifth.attributedText = completeTextm4;
                self.bg.isHidden = false
                self.load.isHidden = true
                self.img.isHidden = false
                
                
            }else if(responsep.result.isFailure){
                print(responsep.result)
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
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
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
