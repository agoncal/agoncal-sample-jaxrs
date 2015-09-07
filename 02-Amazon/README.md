# Samples - JAX-RS - Amazon

## Purpose of this sample

To get data from the Amazon database in terms of books and so on. 

## Amazon database

This relies on the Amazon Access Key ID and Secret Access Key so you should get one to make it work.

* http://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSGettingStartedGuide/AWSCredentials.html
* http://www.cloudberrylab.com/blog/how-to-find-your-aws-access-key-id-and-secret-access-key-and-register-with-cloudberry-s3-explorer/

Then it access the Amazon APIs :

* http://docs.aws.amazon.com/AWSECommerceService/latest/GSG/Welcome.html
* http://docs.aws.amazon.com/AWSECommerceService/latest/DG/Welcome.html
* http://s3.amazonaws.com/awsdocs/Associates/latest/prod-adv-api-qrc.pdf
* http://associates-amazon.s3.amazonaws.com/scratchpad/index.html

### Amazon request example

* Get all the "Java EE 7" books : http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7
* Get all the "Blue Rondo" CDs  : http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=DigitalMusic&Keywords=Blue+Rondo
* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Large&Sort=titlerank&XMLEscaping=Double&ItemPage=1

### Responses groups

Reponse groups controle the data that is being returned. Typical ones are : Large, Medium, Small,Images

* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Small
* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Large
* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Images
ResponseGroup=Images&Title=Java+EE+7

### Paging

AWS only returns a certain number of items. If you want to scroll the rest of the result, use the `ItemPage` parameter :

* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Small
* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Small&ItemPage=4

### Sorting

Results can be sorted using the `Sort` parameter :
 
* http://free.apisigning.com/onca/xml?Service=AWSECommerceService&AWSAccessKeyId=XYZ&Operation=ItemSearch&SearchIndex=Books&Keywords=Java+EE+7&ResponseGroup=Small&Sort=titlerank

# Licensing

<a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/"><img alt="Creative Commons License" style="border-width:0" src="http://i.creativecommons.org/l/by-sa/3.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-sa/3.0/">Creative Commons Attribution-ShareAlike 3.0 Unported License</a>.

<div class="footer">
    <span class="footerTitle"><span class="uc">a</span>ntonio <span class="uc">g</span>oncalves</span>
</div>
