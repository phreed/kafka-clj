

In order to get this to work there are some prerequisites.

The following services:
Install kafka
Update the \*.services files with paths as appropriate.

     sudo cp ./zookeeper.service /lib/systemd/system/zookeeper.service
     sudo systemctl enable zookeeper.service
     sudo systemctl start zookeeper.service

     sudo cp ./kafka.service /lib/systemd/system/kafka.service
     sudo systemctl enable kafka.service
     sudo systemctl start kafka.service

 
