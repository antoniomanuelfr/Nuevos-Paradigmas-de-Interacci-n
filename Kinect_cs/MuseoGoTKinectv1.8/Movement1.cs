using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.ControlsBasics
{
    class Movement1
    {


        private int fase;
        private float factorA, factorB;
        private int cont; 
        public Movement1(float a = 1.3f, float b = 0.7f)
        {
            fase = 0;
            factorA = a;
            factorB = b;
        }

        public int getFase()
        {
            return fase;
        }
        public void setFase(int a)
        {
            fase=a;
        }

        private bool Reposo(Skeleton esqueleto)
        {
            Joint[] puntos = new Joint[3];
            //Tomamos los 6 puntos de referencia para comprobar el estado de reposo
            puntos[0] = esqueleto.Joints[JointType.ShoulderRight];
            puntos[1] = esqueleto.Joints[JointType.ElbowRight];
            puntos[2] = esqueleto.Joints[JointType.WristRight];
            if(puntos[2].Position.Y > puntos[0].Position.Y) // muñeca por encima del hombro
            {
                if( (puntos[2].Position.X > (puntos[1].Position.X * factorB)) && (puntos[2].Position.X < (puntos[1].Position.X * factorA)) )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }


        }
        public bool movimiento(Skeleton esqueleto)
        {
            Joint[] puntos = new Joint[3];
            //Tomamos los 3 puntos de referencia para comprobar el movimiento
            puntos[0] = esqueleto.Joints[JointType.ShoulderRight];
            puntos[1] = esqueleto.Joints[JointType.ElbowRight];
            puntos[2] = esqueleto.Joints[JointType.WristRight];
            if( fase ==0) {
                if (Reposo(esqueleto))
                {
                    fase = 1;
                    //Console.WriteLine("111111111");
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (fase == 1) {
                cont++;
                if ((puntos[2].Position.X > (puntos[1].Position.X * factorB)) && (puntos[2].Position.X < (puntos[1].Position.X * factorA)))
                {
                    if(cont == 20)
                    {
                        cont = 0;
                    }

                    if ((puntos[2].Position.Y < puntos[1].Position.Y))
                    {
                        fase = 2;
                        return true;
                    }

                    else
                    {
                        return true;
                    }
                    
                }
                else
                {
                    fase = 0;
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
    }
}
