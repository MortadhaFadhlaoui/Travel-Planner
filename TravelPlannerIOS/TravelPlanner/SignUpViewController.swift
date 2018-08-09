//
//  SignUpViewController.swift
//  TravelPlanner
//
//  Created by Ahmed Mohamed on 15/11/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class SignUpViewController: UIViewController {
      static var tokensec = ""
   static var connecteduser: UserModel = UserModel()
      var data = Data()
   
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var username: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var numtel: UITextField!
    
    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var btnregister: UIButton!
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        let color = UIColor.white
        email.attributedPlaceholder = NSAttributedString(string: email.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        username.attributedPlaceholder = NSAttributedString(string: username.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        password.attributedPlaceholder = NSAttributedString(string: password.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        numtel.attributedPlaceholder = NSAttributedString(string: numtel.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        load.isHidden = true
        btnregister.layer.cornerRadius = 13
        email.layer.cornerRadius = 13
        username.layer.cornerRadius = 13
        password.layer.cornerRadius = 13
        numtel.layer.cornerRadius = 13
        email.leftViewMode = UITextFieldViewMode.always
        
        let imageView = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let image = UIImage(named: "mail.png")
        
        imageView.image = image
        email.leftView = imageView
        
        username.leftViewMode = UITextFieldViewMode.always
        
        let imageViewp = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagep = UIImage(named: "log.png")
        imageViewp.image = imagep
        username.leftView = imageViewp
        
        password.leftViewMode = UITextFieldViewMode.always
        
        let imageViewpa = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagepa = UIImage(named: "pwd.png")
        imageViewpa.image = imagepa
        password.leftView = imageViewpa
        
        numtel.leftViewMode = UITextFieldViewMode.always
        
        let imageViewn = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagen = UIImage(named: "telephone.png")
        imageViewn.image = imagen
        numtel.leftView = imageViewn
        
        
            email.keyboardType = UIKeyboardType.emailAddress
     
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func register(_ sender: Any) {
        
        if(username.text != "" && password.text != "" && numtel.text != "" && (isValidEmail(testStr: email.text!) == true)  ){
            registeritem(username: username.text!,password: password.text!,num: numtel.text!,role: "traveler", email: email.text!)
            load.isHidden = false
            load.startAnimating()
        }else{
           _ = SweetAlert().showAlert("Error!", subTitle: "Fill out all inputs!", style: AlertStyle.error)

        }
        
     
        
    }
  
    func isValidEmail(testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    
    func registeritem(username: String,password: String,num: String,role: String,email: String) {
        let urlPath = URL(string: NavigatorData.URL+"/api/register?username="+username+"&password="+password+"&role="+role+"&email="+email+"&numtel="+num)
        
        Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
            if(responsep.result.isSuccess){
                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                
                
                let success = dictp?["success"]! as? Int
                if(success?.hashValue==1){
                    let data = dictp?["data"]! as? Dictionary<String, AnyObject>
                 
                    SignUpViewController.tokensec = data?["token"] as! String
                    print(SignUpViewController.tokensec)
                    
                   
                    let user = dictp?["user"]! as? Dictionary<String, AnyObject>
                    
                    
                    
                    
                    
                    let image = user?["image"] as? String
                    let nom = user?["nom"] as? String
                    let prenom = user?["prenom"] as? String
                    let username = user?["username"] as? String
                    let role = user?["role"] as? String
                    let numtel = user?["numtel"]! as? Int
                    let email = user?["email"] as? String
                    let id = user?["id"]! as? Int
                    print(id)
                    SignUpViewController.connecteduser.idUser = id
                    SignUpViewController.connecteduser.nom = nom
                    SignUpViewController.connecteduser.prenom = prenom
                    SignUpViewController.connecteduser.username = username
                    SignUpViewController.connecteduser.role = role
                    SignUpViewController.connecteduser.email = email
                    SignUpViewController.connecteduser.numtel = String(describing: numtel!)
                    SignUpViewController.connecteduser.image = image
                    
                    
                    
                    
                    let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                    let newViewController = storyBoard.instantiateViewController(withIdentifier: "Accueil") as! UINavigationController
                    self.present(newViewController, animated: true, completion: nil)
                }else{
                    
                }
               
               
                
            
                
            }else if(responsep.result.isFailure){
                print("le le")
            }

    }
    /*func itemsDownloaded(items: NSArray) {
        print("getitem")
        feedItems = items
        
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
            
        
    }*/

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
    
}
