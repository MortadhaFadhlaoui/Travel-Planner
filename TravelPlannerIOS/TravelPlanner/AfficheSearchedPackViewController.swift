//
//  AfficheSearchedPackViewController.swift
//  TravelPlanner
//
//  Created by ESPRIT on 13/12/2017.
//  Copyright © 2017 AhmedMohamed. All rights reserved.
//

import UIKit
import Alamofire

class AfficheSearchedPackViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate {

    @IBOutlet weak var load: UIActivityIndicatorView!
    @IBOutlet weak var priceselector: UISlider!
    var paysItemss : [PaysModel]  = []
    var paysItemssarray : [[PaysModel]]  = []
    var secn = 0
    @IBOutlet weak var max: UILabel!
    @IBOutlet weak var min: UILabel!
    @IBOutlet weak var tableView: UITableView!
   
    var dayplanpack: DayPlanerViewController?
    var filteredData : [[PaysModel]] = []
    var isSearching = false
    var uniquePosts:[PackModel] = []
    var uniquePostsearch:[PackModel] = []
    var uniquePostsearchfilter:[PackModel] = []
    var ok:[PackModel] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        self.load.isHidden = false
        self.load.startAnimating()
        
        SearchPackViewController.paksItems.forEach { (p) -> () in
            if !uniquePosts.contains (where: { $0.idPack == p.idPack }) {
                uniquePosts.append(p)
            }
        }
      
        paysItemss = SearchPackViewController.paysItems
       
        for row in 0..<uniquePosts.count {
            // Append an empty row.
            paysItemssarray.append([PaysModel]())
            for i in 0..<paysItemss.count {
                // Populate the row.
                if paysItemss[i].Pack?.idPack == uniquePosts[row].idPack {
                    paysItemssarray[row].append(paysItemss[i])
                }
            }
        }
        var pack: PackModel = PackModel()
        var pack1: PackModel = PackModel()
        ok = SearchPackViewController.paksItems.sorted(by: { $0.prix! > $1.prix! })
        pack1 = ok[ok.startIndex]
        pack = ok[(ok.count-1)]
        
        let x2 = String(describing: pack.prix!)

            min.text =  x2+"$"
        print("ok"+x2)
        let x3 = String(describing: pack1.prix!)
        
        max.text =  x3+"$"
        print("ok"+x3)
        priceselector.minimumValue = Float(pack.prix!)
        priceselector.maximumValue = Float(pack1.prix!)
        priceselector.setValue(Float(pack1.prix!), animated: true)
      tableView.reloadData()
        self.load.isHidden = true
        
        //Long Press
        let longPressGesture:UILongPressGestureRecognizer = UILongPressGestureRecognizer(target: self, action: #selector(handleLongPress))
        longPressGesture.minimumPressDuration = 0.5
        longPressGesture.delegate = self
        self.tableView.addGestureRecognizer(longPressGesture)
        
      
         }
    func handleLongPress(longPressGesture:UILongPressGestureRecognizer) {
        let p = longPressGesture.location(in: self.tableView)
        let indexPath = self.tableView.indexPathForRow(at: p)
        if indexPath == nil {
        }
        else if (longPressGesture.state == UIGestureRecognizerState.began) {
            let aclercont:UIAlertController = UIAlertController(title: "Get the pack", message: "From "+(paysItemssarray[indexPath!.section][indexPath!.row].Pack?.nomDepart!)!+"", preferredStyle: .alert)
            let cancle:UIAlertAction = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
            
            let send:UIAlertAction = UIAlertAction(title: "Send Request", style: .default) { UIAlertAction in
                
                let urlPath = URL(string: NavigatorData.URL+"/api/reserverpack/\((self.paysItemssarray[indexPath!.section][indexPath!.row].Pack?.idPack!)!)")
                let Auth_header    = [ "Authorization" : "Bearer "+SignUpViewController.tokensec ]
                let parameters = [:] as [String : Any]
                Alamofire.request(urlPath!, method: .post,parameters: parameters,encoding: JSONEncoding.default,headers: Auth_header ).responseJSON { responsep in
                    if(responsep.result.isSuccess){
                        let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                        
                        let success = dictp?["success"]! as? NSNumber
                        if(success==1){
                            self.showToast(message: "Your request send successfully")
                        }else{
                            self.showToast(message: "You already send request in that pack")
                        }
                        
                        
                    }else if(responsep.result.isFailure){
                        print("le le")
                    }
                    
                }
                
            }
            aclercont.addAction(send)
            
            aclercont.addAction(cancle)
            self.present(aclercont, animated: true, completion: nil)
            
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView()
        
        view.backgroundColor = UIColor(red: 0/255, green: 154/255, blue: 154/255, alpha: 0.8)
        let label = UILabel()
        
        
        
        if isSearching{
            let x1 = String(describing: (uniquePostsearchfilter[section].prix)!)
            label.text = "This Pack for \(x1)$"
            label.frame = CGRect(x: 10, y: 5, width: 300, height: 35)
            
            view.addSubview(label)
            return view
        }
        let x1 = String(describing: (uniquePosts[section].prix)!)
        label.text = "This Pack for \(x1)$"
        label.frame = CGRect(x: 10, y: 5, width: 300, height: 35)
        
        view.addSubview(label)
        return view
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 50
    }

    
    func numberOfSections(in tableView: UITableView) -> Int {
        if isSearching{
            return uniquePostsearchfilter.count
        }
        return uniquePosts.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if isSearching{
            return filteredData[section].count
        }
        return paysItemssarray[section].count
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell:UITableViewCell = tableView.dequeueReusableCell(withIdentifier: "cell", for: indexPath)
        let pays: PaysModel!
        if isSearching{
            pays = self.filteredData[indexPath.section][indexPath.row]
        }else{
           pays = paysItemssarray[indexPath.section][indexPath.row]
        }
      
        
        let urlPath = URL(string: "https://pixabay.com/api/?key=7171796-c3b099ec431290658803b333a&q=\(pays.nomPays!)+&image_type=photo")
        Alamofire.request(urlPath!,method:.post).responseJSON { responsep in
            if(responsep.result.isSuccess){
                let dictp = responsep.result.value! as? Dictionary<String, AnyObject>
                let groups = dictp?["hits"] as? NSArray
                var array = [String]()
                for places in (groups! as NSArray) {
                    let obj = places as? Dictionary<String, AnyObject>
                    array.append(obj!["webformatURL"] as! String)
                    
                }
                /*let url = NSURL(string: array[0])
                let data = NSData(contentsOf:url! as URL)*/
                
                let imageURL:UIImageView = cell.viewWithTag(1) as! UIImageView
                //imageURL.image = UIImage(data: data! as Data)
                imageURL.sd_setImage(with: URL(string: array[0]), placeholderImage: UIImage(named: "placeholder.png"))
            }else if(responsep.result.isFailure){
                let imageURL:UIImageView = cell.viewWithTag(1) as! UIImageView
                //imageURL.image = UIImage(data: data! as Data)
                imageURL.image = #imageLiteral(resourceName: "placewithno")
            }
            
        }
        
        
        let lblArrive:UILabel = cell.viewWithTag(2) as! UILabel
        lblArrive.text = pays.nomPays
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        formatter.string(from: (pays.Pack?.dateFin!)!)
        let date_d = formatter.string(from: (pays.Pack?.dateDebut!)!).substring(to:formatter.string(from: (pays.Pack?.dateDebut!)!).index(formatter.string(from: (pays.Pack?.dateDebut!)!).startIndex, offsetBy: 10))
        let date_f = formatter.string(from: (pays.Pack?.dateFin!)!).substring(to:formatter.string(from: (pays.Pack?.dateFin!)!).index(formatter.string(from: (pays.Pack?.dateFin!)!).startIndex, offsetBy: 10))
        let lblDateDepart:UILabel = cell.viewWithTag(3) as! UILabel
        lblDateDepart.text = date_d
        
        let lblDateArrive:UILabel = cell.viewWithTag(4) as! UILabel
        lblDateArrive.text = date_f
        
       /* let lblPrix:UILabel = cell.viewWithTag(5) as! UILabel
        let x1 = String(describing: (pays.Pack?.prix)!).substring(to:String(describing: (pays.Pack?.prix)!).index(String(describing: (pays.Pack?.prix)!).endIndex, offsetBy: -2))
        lblPrix.text = x1+"$"*/
        
        return cell
    }

    @IBAction func priceaction(_ sender: Any) {
        
        let currentValue = Int(priceselector.value)
        
        max.text = "\(currentValue)$"
        
        filteredData = []
         uniquePostsearch = []
        uniquePostsearchfilter = []
        isSearching = true
        for row in 0..<paysItemssarray.count {
            // Append an empty row.
            filteredData.append([PaysModel]())
            for i in 0..<paysItemssarray[row].count {
                // Populate the row.
                if (Float((paysItemssarray[row][i].Pack?.prix)!)) <= priceselector.value {
                    filteredData[row].append(paysItemssarray[row][i])
                     uniquePostsearch.append(paysItemssarray[row][i].Pack!)
                }
            }
        }
        
        
        		
        uniquePostsearch.forEach { (p) -> () in
            if !uniquePostsearchfilter.contains (where: { $0.idPack == p.idPack }) {
                uniquePostsearchfilter.append(p)
            }
        }
        self.tableView.reloadData()
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "dayplanshow" {
            let indexrow : Int = (tableView.indexPathForSelectedRow?.row)!
            let indexsection : Int = (tableView.indexPathForSelectedRow?.section)!
            self.dayplanpack = segue.destination as? DayPlanerViewController
            self.dayplanpack?.payspack = paysItemssarray[indexsection][indexrow]
            
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
