#!/bin/sh

cd /opt/inmetdata
java -Xms64m -Xmx256m -classpath /opt/inmetdata:/opt/inmetdata/INMETData.0.1.jar:$CLASSPATH -Djava.library.path=/opt/inmetdata -Djava.security.policy=/opt/inmetdata/inmetdata.policy -Duser.country=BR -Duser.language=pt -Duser.timezone=America/Sao_Paulo  br.embrapa.cnpaf.inmetdata.main.InmetData > /dev/null 
