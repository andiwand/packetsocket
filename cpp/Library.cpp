#include "Library.h"

#include <jni.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/ioctl.h>
#include <sys/socket.h>

#include <net/ethernet.h>

#include <netinet/in.h>

#include <linux/if.h>
#include <linux/if_packet.h>
#include <linux/if_ether.h>
#include <linux/if_arp.h>

#include <iostream>

using namespace std;


int getInterfaceIndex(int socket, const char* interface) {
	struct ifreq req;
	strncpy(req.ifr_name, interface, IFNAMSIZ);
	if (ioctl(socket, SIOCGIFINDEX, &req) < 0)
		return -1;

	return req.ifr_ifindex;
}
sockaddr_ll getLinkLayerSocket(int socket, const char* interface) {
	sockaddr_ll socketAddress;

	socketAddress.sll_family = AF_PACKET;
	socketAddress.sll_ifindex = getInterfaceIndex(socket, interface);
	socketAddress.sll_protocol = htons(ETH_P_ALL);

	return socketAddress;
}
sockaddr_ll getLinkLayerSocket(JNIEnv* env, int socket, jstring interface) {
	jboolean isCopy;
	const char* interfaceName = env->GetStringUTFChars(interface, &isCopy);

	sockaddr_ll socketAddress = getLinkLayerSocket(socket, interfaceName);

	env->ReleaseStringUTFChars(interface, interfaceName);

	return socketAddress;
}

JNIEXPORT jint JNICALL Java_at_stefl_packetsocket_EthernetSocket_openImpl(
		JNIEnv* env, jclass clazz, jint protocol) {
	jint result = socket(PF_PACKET, SOCK_RAW, htons(protocol));

	return result;
}

JNIEXPORT void JNICALL Java_at_stefl_packetsocket_EthernetSocket_closeImpl(
		JNIEnv* env, jclass clazz, jint socket) {
	close(socket);
}

JNIEXPORT void JNICALL Java_at_stefl_packetsocket_EthernetSocket_bindImpl(
		JNIEnv* env, jclass clazz, jint socket, jstring interface) {
	sockaddr_ll socketAddress = getLinkLayerSocket(env, socket, interface);

	bind(socket, (sockaddr*) &socketAddress, sizeof(socketAddress));
}

JNIEXPORT void JNICALL Java_at_stefl_packetsocket_EthernetSocket_enablePromiscModeImpl(
		JNIEnv* env, jclass clazz, jint socket, jstring interface) {
	sockaddr_ll socketAddress = getLinkLayerSocket(env, socket, interface);

	struct packet_mreq mr;
	mr.mr_ifindex = socketAddress.sll_ifindex;
	mr.mr_type = PACKET_MR_PROMISC;
	setsockopt(socket, SOL_PACKET, PACKET_ADD_MEMBERSHIP, &mr, sizeof(mr));
}

JNIEXPORT jint JNICALL Java_at_stefl_packetsocket_EthernetSocket_receiveImpl(
		JNIEnv* env, jclass clazz, jint socket, jbyteArray buffer, jint offset,
		jint length, jint flags) {
	jboolean isCopy;
	jbyte* bufferPointer = env->GetByteArrayElements(buffer, &isCopy);

	int result = recv(socket, bufferPointer, env->GetArrayLength(buffer),
			flags);

	env->ReleaseByteArrayElements(buffer, bufferPointer, 0);

	return result;
}

JNIEXPORT jint JNICALL Java_at_stefl_packetsocket_EthernetSocket_receiveFromImpl(
		JNIEnv* env, jclass clazz, jint socket, jstring interface,
		jbyteArray buffer, jint offset, jint length, jint flags) {
	jboolean isCopy;
	jbyte* bufferPointer = env->GetByteArrayElements(buffer, &isCopy);

	sockaddr_ll socketAddress = getLinkLayerSocket(env, socket, interface);
	unsigned int socketAddressSize = sizeof(socketAddress);
	int result = recvfrom(socket, bufferPointer, env->GetArrayLength(buffer),
			flags, (sockaddr*) &socketAddress, &socketAddressSize);

	env->ReleaseByteArrayElements(buffer, bufferPointer, 0);

	return result;
}

JNIEXPORT jint JNICALL Java_at_stefl_packetsocket_EthernetSocket_sendImpl(
		JNIEnv* env, jclass clazz, jint socket, jbyteArray buffer, jint offset,
		jint length, jint flags) {
	jboolean isCopy;
	jbyte* bufferPointer = env->GetByteArrayElements(buffer, &isCopy);

	int result = send(socket, bufferPointer, env->GetArrayLength(buffer),
			flags);

	env->ReleaseByteArrayElements(buffer, bufferPointer, 0);

	return result;
}

JNIEXPORT jint JNICALL Java_at_stefl_packetsocket_EthernetSocket_sendToImpl(
		JNIEnv* env, jclass clazz, jint socket, jstring interface,
		jbyteArray buffer, jint offset, jint length, jint flags) {
	jboolean isCopy;
	jbyte* bufferPointer = env->GetByteArrayElements(buffer, &isCopy);

	sockaddr_ll socketAddress = getLinkLayerSocket(env, socket, interface);
	int result = sendto(socket, bufferPointer, env->GetArrayLength(buffer),
			flags, (struct sockaddr*) &socketAddress, sizeof(socketAddress));

	env->ReleaseByteArrayElements(buffer, bufferPointer, 0);

	return result;
}
