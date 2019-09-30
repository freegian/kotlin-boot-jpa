import * as pulumi from "@pulumi/pulumi";
import * as eks from "@pulumi/eks";
import * as aws from "@pulumi/aws";
import * as awsx from "@pulumi/awsx";
import {VpcArgs} from "@pulumi/awsx/ec2";

const config = new pulumi.Config()
// ECRリポジトリー作成
const repository = new aws.ecr.Repository(config.require('ECR_REPOSITORY_NAME'), {
  name: config.require('ECR_REPOSITORY_NAME'),
  imageTagMutability: 'IMMUTABLE'
})
// VPC作成
const vpc = new awsx.ec2.Vpc(config.require('VPC_NAME'), {
  numberOfAvailabilityZones: 2, // ← サブネットの数指定。アベイラビリティゾーン分だけpublic/privateサブネットが作成される
  instanceTenancy: 'default',
  numberOfNatGateways: 1, // ← NatGateway数の指定
  cidrBlock: config.require('VPC_CIDR_BLOCK'), // VPCのCIDR指定
  tags: { Name: config.require('VPC_NAME') } // VPCの名前
} as VpcArgs);
const managedPolicyArns: string[] = [
  "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy",
  "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy",
  "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly",
];
const createRole = (name: string): aws.iam.Role => {
  const role = new aws.iam.Role(name, {
    assumeRolePolicy: aws.iam.assumeRolePolicyForPrincipal({
      Service: "ec2.amazonaws.com",
    }),
  })
  let counter = 0;
  for (const policy of managedPolicyArns) {
    const rpa = new aws.iam.RolePolicyAttachment(`${name}-policy-${counter++}`,
      { policyArn: policy, role: role },
    )
  }
  return role;
}
const role = createRole(`${config.require('EKS_CLUSTER_NAME')}-node-role`);
const instanceProfile = new aws.iam.InstanceProfile(`${config.require('EKS_CLUSTER_NAME')}-node-profile`, {role: role});
new eks.Cluster(config.require('EKS_CLUSTER_NAME'), {
  vpcId: vpc.id,
  privateSubnetIds: vpc.privateSubnetIds,
  publicSubnetIds: vpc.publicSubnetIds,
  nodeSubnetIds: vpc.privateSubnetIds,
  deployDashboard: false,
  skipDefaultNodeGroup: true,
  instanceRole: role,
  tags: {Name: config.require('EKS_CLUSTER_NAME')}
} as eks.ClusterOptions).createNodeGroup(`${config.require('EKS_CLUSTER_NAME')}-node`, {
  instanceType: 't2.micro',
  instanceProfile: instanceProfile,
  minSize: 1,
  maxSize: 2
} as eks.ClusterNodeGroupOptions);
