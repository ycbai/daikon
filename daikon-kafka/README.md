Service kafka
==============

This module provides
* a kafka wrapper which tries to send kafka message and store these messages when sending fail
* a spring scheduler which tries periodically to send stored kafka messages
 
This module bases itself on daikon-mongo to store kafka messages

