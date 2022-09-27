#include <stdio.h> 
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include<arpa/inet.h>
#include<string.h>


int main(int argc, char *argv[]) {

  /* parametres : IP, numéro port serveur, numéro port perso)*/

  if (argc != 4){
    printf("utilisation : %s ip_serveur port_serveur port_perso\n", argv[0]);
    exit(0);
  }

  /* Creation socket Pour Server*/


  int ds = socket(AF_INET, SOCK_STREAM,0);

  if (ds == -1){
    printf("Client : pb creation socket\n");
    exit(1);
  }
  printf("Client: creation de la socket : ok\n");
  
  /* contient adresse socket serveur */

  struct sockaddr_in adrServ;
    adrServ.sin_family = AF_INET;
    inet_pton(AF_INET,argv[1],&(adrServ.sin_addr));
    adrServ.sin_port = htons(atoi(argv[2]));
      
  int lgAdr = sizeof(struct sockaddr_in);



  /* creation socket permet recevoir demandes connexion.*/

  int dsVoisin = socket(PF_INET, SOCK_STREAM, 0);

  if (dsVoisin == -1){
    perror("client : probleme creation socketVoisin");
    exit(1);
  }
  
   printf("client : creation de la socketVoisin : ok\n");
 
  
  /* nommage socket
     Elle aura une ou des IP de la machine sur laquelle 
     le programme sera exécuté et le numéro de port passé
     en paramètre
  */

  struct sockaddr_in clientVoisin;
  clientVoisin.sin_family = AF_INET;
  clientVoisin.sin_addr.s_addr = INADDR_ANY;
  clientVoisin.sin_port = htons(atoi(argv[3]));
  
  if(bind(ds,(struct sockaddr*) &clientVoisin, sizeof(clientVoisin)) < 0){
    perror("client : erreur bind");
    close(ds);
    exit(1);
  }

  printf("client : nommage : ok\n");

  /* demande de connexion au serveur.*/

  int conn = connect(ds,(struct sockaddr*) &adrServ,lgAdr);
 
  if (conn <0){
    perror ("Client: pb de connect :");
    close (ds);
    exit (1);
  }
  printf("Client : demande de connexion reussie \n");
  //printf("Client : addresse server : %u \n", adrServ.sin_addr.s_addr);
  
  /* saisie port socket voisin. */

  unsigned short port_perso[1] = {atoi(argv[3])};
 
  printf("port à utiliser pour communiquer avec voisin : %i \n", port_perso[0]);
 

    /*envoi message port*/
  int snd = send(ds,port_perso,sizeof(port_perso),0);

if (snd < 0){
    perror("client : probleme envoi\n");
    close (ds);
    exit (1);
  }
  else if (snd == 0)
  {
    perror("client : socket fermée ?!?\n");
    close (ds);
    exit (1);
  }

  printf("Client : j'ai déposé %d octets (message)\n", snd);



  // /* reception réponse serveur = nb octets message reçu */

  // printf("Client : envois faits, j'attends la reponse du serveur \n");
  
  // int reponse;
  // int rcv = recv (ds, &reponse, sizeof(reponse),0) ;
  
  // if (rcv < 0){
  //   perror("Client : probleme reception\n");
  //   close (ds);
  //   exit (1);
  // }
  // else if (rcv == 0)
  // {
  //   printf("Client : socket fermée ?!?\n");
  //   close (ds);
  //   exit (1);
  // }

  // /* compare nboctets déposés & val reçue
  //    objectif avoir même valeur. */

  // printf("Client : j'ai envoyé %d octets et le serveur me répond qu'il a reçu : %d octets \n", snd+snd2, reponse) ;

  // /* Fin */

  close (ds);
  printf("Client : je termine\n");
}
