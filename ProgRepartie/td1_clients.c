#include <stdio.h> 
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include<arpa/inet.h>
#include<string.h>


void sep(char* debut,char* fin,char* tab, int size, char c){
 int i = 0;
 while(tab[i]!=c){
   debut[i] = tab[i];
   i++;
 }
 
 i++;
 
 while(i < size) {
   fin[i] = tab[i];
   i++;
 }
}

int main(int argc, char *argv[]) {

  /* parametres : IP, numéro port serveur, numéro port perso)*/

  if (argc != 4){
    printf("utilisation : %s ip_serveur port_serveur port_perso\n", argv[0]);
    exit(0);
  }

  /* Creation socket Pour Server*/


  int socketServ = socket(AF_INET, SOCK_STREAM,0);

  if (socketServ == -1){
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

  int socketVoisin = socket(PF_INET, SOCK_STREAM, 0);

  if (socketVoisin == -1){
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
  
  if(bind(socketVoisin,(struct sockaddr*) &clientVoisin, sizeof(clientVoisin)) < 0){
    perror("client : erreur bind");
    close(socketVoisin);
    exit(1);
  }

  printf("client : nommage : ok\n");

  /* demande de connexion au serveur.*/
  printf("client : adresse server : %s \n", inet_ntoa(adrServ.sin_addr));
  printf("client : PORT server : %d \n", ntohs(adrServ.sin_port));


  int conn = connect(socketServ,(struct sockaddr*) &adrServ,lgAdr);
  printf("client : conn : %d \n", conn);
 
  if (conn <0){
    perror ("Client: pb de connect :");
    close (socketServ);
    exit (1);
  }
  printf("Client : demande de connexion reussie \n");
  //printf("Client : addresse server : %u \n", adrServ.sin_addr.s_addr);
  
  /* saisie port socket voisin. */

  unsigned short port_perso[1] = {atoi(argv[3])};
 
  printf("port à utiliser pour communiquer avec voisin : %i \n", port_perso[0]);
 

    /*envoi message port*/
  int snd = send(socketServ,port_perso,sizeof(port_perso),0);

if (snd < 0){
    perror("client : probleme envoi\n");
    close (socketServ);
    exit (1);
  }
  else if (snd == 0)
  {
    perror("client : socket fermée ?!?\n");
    close (socketServ);
    exit (1);
  }

  printf("Client : j'ai déposé %d octets (message)\n", snd);

  //reception ip:port à contacter

  char buffer[sizeof(char)+sizeof(clientVoisin.sin_addr.s_addr)+sizeof(clientVoisin.sin_port)];

  int rcv = recv(socketServ, buffer, sizeof(buffer),0) ;

	if (socketServ < 0){ 
		perror ( "client : probleme reception :");
		close(socketServ);
		close(socketVoisin);
		exit (1);
	}
	else if (socketServ == 0)
	{
		printf("client : socket serveur fermée ?!?\n");
		close(socketServ);
		close(socketVoisin);
		exit (1);
	}

	printf("Serveur : j'ai recu %d octets \n", rcv);
	printf("Serveur : contenu du message : %i \n", buffer[0]);




	//socket demande connexion 
	int dsEnvoi = socket(AF_INET, SOCK_STREAM,0);

	if (dsEnvoi == -1){
		printf("Client : pb creation socket Envoi\n");
		exit(1);
	}
		printf("Client: creation de la socket Envoi : ok\n");
  
  /* contiendra adresse ip socket Envoi */
		char ipEnvoi[sizeof(unsigned long)];

  /* contiendra port socket Envoi */
		char portEnvoi[sizeof(short unsigned int)];

		sep(ipEnvoi, portEnvoi, buffer, sizeof(buffer), ':');


  //CRÉATION SOCKET ENVOI
  struct sockaddr_in adrEnvoi;
    adrEnvoi.sin_family = AF_INET;
    inet_pton(AF_INET, ipEnvoi, &(adrEnvoi.sin_addr));
    adrEnvoi.sin_port = atoi(portEnvoi);
      
  int lgAdr2 = sizeof(struct sockaddr_in);

  //MISE EN ECOUTE POUR ENTRÉE
  int ecoute = listen(socketVoisin,1);
  if (ecoute < 0){
    printf("client : je suis sourd(e)\n");
    close (socketVoisin);
    exit (1);
  } 
 
  printf("client: J'attends mon voisin de gauche : ok\n");

  //CONNEXION SOCKET VOISIN (ENVOI)
  conn = connect(dsEnvoi,(struct sockaddr*) &adrEnvoi,lgAdr2);
 
  if (conn <0){
    perror ("Client: pb de connect :");
    close (dsEnvoi);
    exit (1);
  }
  printf("\n\nClient : demande de connexion au voisin reussie ?\n");


  while(1){
  	printf("client : j'attends la demande du voisin (accept) \n");

	struct sockaddr_in adCv ; // obtenir adresse client accepté
	socklen_t lgCv = sizeof (struct sockaddr_in);

	int voisacc = accept(socketVoisin,(struct sockaddr *) &adCv, &lgCv);
	if (voisacc < 0){
		perror ( "client, probleme accept :");
		close(socketVoisin);
		exit (1);
	}

	/* affichage adresse socket client accepté :
	 adresse IP et numéro de port de structure adCv. 
	 Attention conversions format réseau -> format hôte.
	 fonction inet_ntoa(..) pour l'IP. */

	// char* ipserv = inet_ntoa(adCv.sin_addr);
	// int port = htons(server.sin_port);
	printf("client: le voisin est connecté  \n");


	  //fermeture socket server à la fin
	}
	close (socketServ);
	close (socketVoisin);
	printf("Client : je termine\n");
}

