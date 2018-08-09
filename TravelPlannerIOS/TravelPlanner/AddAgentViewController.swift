//
//  AddAgentViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 08/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire
class AddAgentViewController: UIViewController {

    @IBOutlet weak var btnregister: UIButton!
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var username: UITextField!
    @IBOutlet weak var password: UITextField!
    @IBOutlet weak var phone: UITextField!
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()

        
        let color = UIColor.white
        email.attributedPlaceholder = NSAttributedString(string: email.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        username.attributedPlaceholder = NSAttributedString(string: username.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        password.attributedPlaceholder = NSAttributedString(string: password.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        phone.attributedPlaceholder = NSAttributedString(string: phone.placeholder!, attributes: [NSForegroundColorAttributeName : color])
        
        btnregister.layer.cornerRadius = 13
        email.layer.cornerRadius = 13
        username.layer.cornerRadius = 13
        password.layer.cornerRadius = 13
        phone.layer.cornerRadius = 13
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
        
        phone.leftViewMode = UITextFieldViewMode.always
        
        let imageViewn = UIImageView(frame: CGRect(x: 0, y: 0, width: 20, height: 20))
        let imagen = UIImage(named: "telephone.png")
        imageViewn.image = imagen
        phone.leftView = imageViewn
        
        
        email.keyboardType = UIKeyboardType.emailAddress
        
        
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func isValidEmail(testStr:String) -> Bool {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
        return emailTest.evaluate(with: testStr)
    }
    @IBAction func addagent(_ sender: Any) {
        
        if(username.text != "" && password.text != "" && phone.text != "" && (isValidEmail(testStr: email.text!)==true)){
            SweetAlert().showAlert("Are you sure?", subTitle: "The agent will be added!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, Add him!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
                if isOtherButton == true {
                    
                   _ = SweetAlert().showAlert("Cancelled!", subTitle: "Agent cancelled", style: AlertStyle.error)
                }
                else {
                    self.registeritem(username: self.username.text!,password: self.password.text!,num: self.phone.text!,role: "agent", email: self.email.text!)
                  _ =  SweetAlert().showAlert("Added!", subTitle: "Agent has been added!", style: AlertStyle.success)
                }
        }
       
            
            
        }else{
           _ = SweetAlert().showAlert("Error!", subTitle: "Please fill out all inputs!", style: AlertStyle.error)

        }
        
        
        
        
        
        
        
        
    }
    func registeritem(username: String,password: String,num: String,role: String,email: String) {
        let urlPath = URL(string: NavigatorData.URL+"/api/register?username="+username+"&password="+password+"&role="+role+"&email="+email+"&numtel="+num)
                Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
            if(responsep.result.isSuccess){
                self.username.text = ""
                self.password.text = ""
                self.email.text = ""
                self.phone.text = ""
                self.showToast(message: "Added successfully")
            }else if(responsep.result.isFailure){
                print("le le")
            }
            
        }

    
 
    }
    
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
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
