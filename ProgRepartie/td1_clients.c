#include <stdio.h> 
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <string.h>
#include <fcntl.h>
#include <errno.h>

void sep(char* debut,char* fin,char* tab, int size, char c){
 int i = 0;
 //printf("%i\n",size);
 while(tab[i]!=c){
   debut[i] = tab[i];
   i++;
 }
 
 i++;
 int j = 0;
 while(i < size) {
   fin[j] = tab[i];
   //printf("%c\n",tab[i] );
   j++;
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
  //printf("client : conn : %d \n", conn);
 
  if (conn <0){
    perror ("Client: pb de connect :");
    close (socketServ);
    exit (1);
  }
  printf("Client : demande de connexion reussie \n");
  //printf("Client : addresse server : %u \n", adrServ.sin_addr.s_addr);
  
  /* saisie port socket voisin. */

  int taille = (int) sizeof(inet_ntoa(clientVoisin.sin_addr))+1+sizeof(argv[3]);

  char big [taille];
  memset(big, 0, taille);

printf(" big : %s\n",big);

  strcat(big,inet_ntoa(clientVoisin.sin_addr));
  strcat(big,":"); 
  strcat(big, argv[3]);

  printf("sizeof big : %li\n",sizeof(big) );

  // unsigned short port_perso[1] = {atoi(argv[3])};
  
  // char * adresse_perso[sizeof(unsigned long)];

  // adresse_perso[0] = inet_ntoa(clientVoisin.sin_addr);
 
  printf("\n\n adresse et port à utiliser pour communiquer avec voisin : %s \n", big);
 

    /*envoi message port*/
  int snd = send(socketServ,big,sizeof(big),0);

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

  // int snd2 = send(socketServ,port_perso,sizeof(port_perso),0);
  // if (snd2 < 0){
  //   perror("client : probleme envoi\n");
  //   close (socketServ);
  //   exit (1);
  // }
  // else if (snd2 == 0)
  // {
  //   perror("client : socket fermée ?!?\n");
  //   close (socketServ);
  //   exit (1);
  // }

  // printf("Client : j'ai déposé %d octets (message)\n", snd2);


  //reception ip:port à contacter

  char buffer[100];

  int rcv = recv(socketServ, buffer, sizeof(buffer),0);
  printf("hello coucou recv%d\n",rcv );

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
	printf("Serveur : contenu du message : %s \n", buffer);




	//socket demande connexion 
	int dsEnvoi = socket(AF_INET, SOCK_STREAM,0);

	if (dsEnvoi == -1){
		printf("Client : pb creation socket Envoi\n");
		exit(1);
	}
		printf("Client: creation de la socket Envoi : ok\n");
  
  /* contiendra adresse ip socket Envoi */
		char ipEnvoi[20];

  /* contiendra port socket Envoi */
		char portEnvoi[10];

		sep(ipEnvoi, portEnvoi, buffer, rcv, ':');

		printf("\n\n adresse %s:%s\n",ipEnvoi,portEnvoi );


  //CRÉATION SOCKET ENVOI
  struct sockaddr_in adrEnvoi;
    adrEnvoi.sin_family = AF_INET;
    inet_pton(AF_INET, ipEnvoi, &(adrEnvoi.sin_addr));
    adrEnvoi.sin_port = atoi(portEnvoi);
      
  int lgAdr2 = sizeof(struct sockaddr_in);

  //MISE EN ECOUTE POUR ENTRÉE
  int flags = fcntl(socketVoisin, F_GETFL);
  fcntl(socketVoisin, F_SETFL, flags | O_NONBLOCK);

  int ecoute = listen(socketVoisin,1);
  if (ecoute < 0){
    printf("client : je suis sourd(e)\n");
    close (socketVoisin);
    exit (1);
  } 
 


 ////

// recevoir infos server

  ////
  printf("client: J'attends mon voisin de gauche : ok\n");

  //CONNEXION SOCKET VOISIN (ENVOI)
	struct sockaddr_in adCv ; // obtenir adresse client accepté
	socklen_t lgCv = sizeof (struct sockaddr_in);

  while(accept(socketVoisin,(struct sockaddr *) &adCv, &lgCv)==-1)
  {printf("ERRNO = %d\n",errno);
    conn = 25000;
  	conn = connect(dsEnvoi,(struct sockaddr*) &adrEnvoi,lgAdr2);
	  if (conn <0){
	    perror ("Client: pb de connect : ");
	    printf("conn = %d\n", conn);
	  }else printf("\n\nClient : demande de connexion au voisin reussie ?\n");
  	
  	if (errno == EWOULDBLOCK) {
        printf("No pending connections; sleeping for one second.\n");
        sleep(2);
      } else {
        perror("error when accepting connection");
        exit(1);
      }
  }
 



  	printf("client : sortie de boucle connection acceptée \n");


	// int voisacc = accept(socketVoisin,(struct sockaddr *) &adCv, &lgCv);
	// if (voisacc < 0){
	// 	perror ( "client, probleme accept :");
	// 	close(socketVoisin);
	// 	exit (1);
	// }

	/* affichage adresse socket client accepté :
	 adresse IP et numéro de port de structure adCv. 
	 Attention conversions format réseau -> format hôte.
	 fonction inet_ntoa(..) pour l'IP. */

	// char* ipserv = inet_ntoa(adCv.sin_addr);
	// int port = htons(server.sin_port);
	printf("client: le voisin est connecté  \n");


	  //fermeture socket server à la fin
	
	close (socketServ);
	close (socketVoisin);
	printf("Client : je termine\n");
}

