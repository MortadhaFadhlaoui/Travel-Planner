//
//  AddPackThirdViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/17.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import JKSteppedProgressBar
import D2PDatePicker
import Alamofire
class AddPackThirdViewController: UIViewController ,SCPopDatePickerDelegate, UITableViewDelegate, UITableViewDataSource{
    static var pack: PackModel = PackModel()
  
    static var pays : [String]  = []
    static var days : [DayPanModel]  = [DayPanModel]()
    var feedItems : [String]  = []
    var feedItems1 : [String]  = []
  
    let datePicker = SCPopDatePicker()
    let date = Date()
    lazy var dateFormatter: DateFormatter = {
        
        var formatter = DateFormatter()
        formatter.timeStyle = .none
        formatter.dateStyle = .medium
        return formatter
    }()
    
    var dateComponent = DateComponents()
    var departtxt: String = ""
    var datedeparttxt: Date? = nil
    var x = 2
    
    
    
    @IBOutlet weak var descriptionn: UITextView!
    
    @IBOutlet weak var titlee: UITextField!
    @IBOutlet weak var tableview: UITableView!
  
    @IBOutlet weak var datedepart: UIButton!
       @IBOutlet weak var prog: SteppedProgressBar!
    
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        
        descriptionn.placeholder = "Please enter a description of your Day plan"
        
        
        
        
        
        print(AddPackThirdViewController.pays)
        prog.stepDrawingMode = .fill
        
        prog.titles = ["Step 1".localized, "Step 2".localized, "Step 3".localized,]
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    @IBAction func back(_ sender: Any) {
        
        
        
        
        let controller = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "secondaddpack")
        self.show(controller, sender: true)
    }
    
    @IBAction func selectday(_ sender: Any) {
      
        self.datePicker.tapToDismiss = true
        self.datePicker.datePickerType = SCDatePickerType.date
        self.datePicker.showBlur = true
        self.datePicker.datePickerStartDate = self.date
        self.datePicker.btnFontColour = UIColor.white
        self.datePicker.btnColour = UIColor.blue
        self.datePicker.showCornerRadius = false
        self.datePicker.delegate = self
        self.datePicker.show(attachToView: self.view)
        x = 1
        
    }
   
    
    func UIColorFromRGB(rgbValue: UInt) -> UIColor {
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
    @IBAction func next(_ sender: Any) {
        
        
        SweetAlert().showAlert("Are you sure?", subTitle: "The pack will be added!", style: AlertStyle.warning, buttonTitle:"No, cancel!", buttonColor:UIColorFromRGB(rgbValue: 0xD0D0D0) , otherButtonTitle:  "Yes, add it!", otherButtonColor: UIColorFromRGB(rgbValue: 0xDD6B55)) { (isOtherButton) -> Void in
            if isOtherButton == true {
                
              _ =  SweetAlert().showAlert("Cancelled!", subTitle: "Pack not added", style: AlertStyle.error)
            }
            else {
               
                
                
                let dateFormatter1 = DateFormatter()
                dateFormatter1.dateFormat = "YYYY-MM-dd" //Your date format
                dateFormatter1.timeZone = TimeZone(abbreviation: "GMT+0:00")
                let d = dateFormatter1.string(from: AddPackThirdViewController.pack.dateDebut!)
                
                let x1 = d.substring(to:d.index(d.startIndex, offsetBy: 10))
                
                let d2 = dateFormatter1.string(from: AddPackThirdViewController.pack.dateFin!)
                
                let x2 = d2.substring(to:d2.index(d2.startIndex, offsetBy: 10))
                
                
                print()
                
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                
                let urlPath = URL(string: NavigatorData.URL+"/api/SavePack?depart="+AddPackThirdViewController.pack.nomDepart!+"&price="+String(describing: AddPackThirdViewController.pack.prix!)+".00&datedepart="+x1+"&datea="+x2)
                
                
                Alamofire.request(urlPath!,method:.post,headers: Auth_header ).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        
                        let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                        let success = dictp?["id"]! as? Int
                        for item in AddPackThirdViewController.pays {
                            
                            let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                            
                            let urlPath1 = URL(string: NavigatorData.URL+"/api/SavePays?etat="+String(describing:AddPackThirdViewController.pays.count)+"&nom="+item+"&id="+String(describing: success!) )
                            
                            
                            Alamofire.request(urlPath1!, method: .post,headers: Auth_header ).responseJSON { responsep in
                                if(responsep.result.isSuccess){
                                    
                                }else if(responsep.result.isFailure){
                                    print(responsep.result)
                                }
                                
                            }
                            
                        }
                        
                        if(AddPackThirdViewController.days.count != 0){
                            
                            let dateFormatter1 = DateFormatter()
                            dateFormatter1.dateFormat = "YYYY-MM-dd" //Your date format
                            dateFormatter1.timeZone = TimeZone(abbreviation: "GMT+0:00")
                            print(AddPackThirdViewController.days)
                            print(AddPackThirdViewController.days.count)
                            
                            for item1 in AddPackThirdViewController.days {
                                
                                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec]
                                
                                
                                let d10 = dateFormatter1.string(from: item1.date!)
                                
                                let x10 = d10.substring(to:d10.index(d10.startIndex, offsetBy: 10))
                                
                                
                                
                                let urlPathhh = NavigatorData.URL+"/api/SaveDay?title="+item1.title!+"&description="+item1.descriptionday!
                                let urlPath2 = URL(string: urlPathhh+"&date="+x10+"&heure=12:00"+"&id="+String(describing: success!) )
                                
                                
                                
                                Alamofire.request(urlPath2!, method: .post,headers: Auth_header ).responseJSON { responsep in
                                    if(responsep.result.isSuccess){
                                        
                                        
                                        
                                        
                                        
                                        
                                    }else if(responsep.result.isFailure){
                                        print(responsep.result)
                                    }
                                    
                                }
                                
                                
                                
                            }
                            
                            
                        }
                        
                        self.showToast(message: "Added successfully")
                        
                        let when = DispatchTime.now() + 2
                        DispatchQueue.main.asyncAfter(deadline: when) {
                            let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                            let newViewController = storyBoard.instantiateViewController(withIdentifier: "agenthome") as! UITabBarController
                            self.present(newViewController, animated: true, completion: nil)
                        }
                        
                        
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
               _ = SweetAlert().showAlert("Added!", subTitle: "Pack has been added!", style: AlertStyle.success)
            }
        }
        
        
        
        
        
        
        
        
        
    }
    func scPopDatePickerDidSelectDate(_ date: Date) {
        
        if x == 1 {
            datedeparttxt = date
            datedepart.setTitle(dateFormatter.string(from: datedeparttxt!) , for: .normal)
        }
        
        
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return self.feedItems.count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "mydayplancelladd", for: indexPath)
        let NameCountry: String!
        
        NameCountry = self.feedItems1[indexPath.row]
        
        
        //cell.textLabel?.text = "Hello from new Cell"
        
        let lblCountry:UILabel = cell.viewWithTag(1) as! UILabel
        lblCountry.text = NameCountry
        
        
        
        let NameCountry1: String!
        
        NameCountry1 = self.feedItems[indexPath.row]
        
        
        //cell.textLabel?.text = "Hello from new Cell"
        
        let lblCountry1:UILabel = cell.viewWithTag(2) as! UILabel
        lblCountry1.text = NameCountry1
        
        
        
        return cell
    }
    
    @IBAction func addme(_ sender: Any) {
        
        if(titlee.text! != "" && datedeparttxt != nil && descriptionn.text! != ""){
            
            let p:DayPanModel = DayPanModel()
            p.title = titlee.text!
            p.descriptionday = descriptionn.text!
            p.date = datedeparttxt
            
            AddPackThirdViewController.days.append(p)
            
            self.feedItems.append(titlee.text!)
            self.feedItems1.append(dateFormatter.string(from: datedeparttxt!) )
            
            self.tableview.reloadData()
        }else{
            showToast(message: "fill out all inputs")
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

  
}
extension UITextView: UITextViewDelegate {
    
    /// Resize the placeholder when the UITextView bounds change
    override open var bounds: CGRect {
        didSet {
            self.resizePlaceholder()
        }
    }
    
    /// The UITextView placeholder text
    public var placeholder: String? {
        get {
            var placeholderText: String?
            
            if let placeholderLabel = self.viewWithTag(100) as? UILabel {
                placeholderText = placeholderLabel.text
            }
            
            return placeholderText
        }
        set {
            if let placeholderLabel = self.viewWithTag(100) as! UILabel? {
                placeholderLabel.text = newValue
                placeholderLabel.sizeToFit()
            } else {
                self.addPlaceholder(newValue!)
            }
        }
    }
    
    /// When the UITextView did change, show or hide the label based on if the UITextView is empty or not
    ///
    /// - Parameter textView: The UITextView that got updated
    public func textViewDidChange(_ textView: UITextView) {
        if let placeholderLabel = self.viewWithTag(100) as? UILabel {
            placeholderLabel.isHidden = self.text.characters.count > 0
        }
    }
    
    /// Resize the placeholder UILabel to make sure it's in the same position as the UITextView text
    private func resizePlaceholder() {
        if let placeholderLabel = self.viewWithTag(100) as! UILabel? {
            let labelX = self.textContainer.lineFragmentPadding
            let labelY = self.textContainerInset.top - 2
            let labelWidth = self.frame.width - (labelX * 2)
            let labelHeight = placeholderLabel.frame.height
            
            placeholderLabel.frame = CGRect(x: labelX, y: labelY, width: labelWidth, height: labelHeight)
        }
    }
    
    /// Adds a placeholder UILabel to this UITextView
    private func addPlaceholder(_ placeholderText: String) {
        let placeholderLabel = UILabel()
        
        placeholderLabel.text = placeholderText
        placeholderLabel.sizeToFit()
        
        placeholderLabel.font = self.font
        placeholderLabel.textColor = UIColor.lightGray
        placeholderLabel.tag = 100
        
        placeholderLabel.isHidden = self.text.characters.count > 0
        
        self.addSubview(placeholderLabel)
        self.resizePlaceholder()
        self.delegate = self
    }
    
}
