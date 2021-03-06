(ns vignette.storage.static-assets-test
  (:require [midje.sweet :refer :all]
            [org.httpkit.client :as http]
            [vignette.storage.protocols :refer :all]
            [vignette.storage.static-assets :as sa]
            ))

(facts :static-assets :get-original
       (get-original
         (sa/create-static-image-storage --static-asset-get--) {:uuid ..uuid..}) => ..object..
       (provided
         (--static-asset-get-- ..uuid..) => ..static-asset-url..
         (http/get ..static-asset-url.. {:as :stream}) => (future {:status 200})
         (sa/->AsyncResponseStoredObject {:status 200}) => ..object..)
       (get-original
         (sa/create-static-image-storage --static-asset-get--) {:uuid ..uuid.. :options {:status ..statuses..}}) => nil
       (provided
         (--static-asset-get-- ..uuid..) => ..static-asset-url..
         (http/get ..static-asset-url.. {:as :stream}) => (future {:status 404})))

(facts :static-assets :filename
       (filename
         (sa/->AsyncResponseStoredObject ..response..)) => "filename.png"
       (provided
         ..response.. =contains=> {:headers {:content-disposition "Filename=\"filename.png\""}})
       (filename
         (sa/->AsyncResponseStoredObject ..response..)) => nil
       (provided
         ..response.. =contains=> {:headers {:content-disposition "qilename=\"filename.png\""}})
       (filename
         (sa/->AsyncResponseStoredObject ..response..)) => nil
       (provided
         ..response.. =contains=> {:headers {}})
       (filename
         (sa/->AsyncResponseStoredObject ..response..)) => nil)
