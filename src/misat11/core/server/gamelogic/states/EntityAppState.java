/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.gamelogic.states;

import com.jme3.app.state.AbstractAppState;
import com.jme3.math.Vector3f;
import misat11.core.server.Main;
import misat11.core.server.ServerDataStorage;
import misat11.core.server.Utils;
import misat11.core.server.messages.guis.CloseGuiMessage;
import misat11.core.server.messages.guis.OpenGuiMessage;
import misat11.core.server.objects.Entity;
import misat11.core.server.reasons.DeathReason;

/**
 *
 * @author misat11
 */
public class EntityAppState extends AbstractAppState {

    private int id;
    private Entity entity;
    private ServerDataStorage serverDataStorage;

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean jump = false;

    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

    private boolean attacked = false;
    private float attackedtime = 0f;
    private float maxAttackedtime = 0.5f;

    private float lastOnGroundY = 0f;
    private float highestAirY = 0f;

    private float damage_for_one_float = 2f;
    private float height_without_damage = 4f;

    private float second_of_burn_to_damage = 10f;
    private float burnTime = 0f;
    private boolean inFire = false;
    private boolean burn = false;
    private float burn_time_after_exit_fire = 3.5f;

    private boolean inwater = false;
    private float time_in_water = 0f;
    private float seconds_to_drown = 10f;
    private boolean drown = false;
    private float drown_time = 0f;
    private float drown_damage_for_second = 35f;

    private Main main;
    
    public EntityAppState(int id, Main main, Entity entity, ServerDataStorage serverDataStorage) {
        this.id = id;
        this.main = main;
        this.entity = entity;
        this.serverDataStorage = serverDataStorage;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void damage(float damage) {
        if ((entity.getHealth() - damage) < 0) {
            entity.setHealth(0);
        } else {
            entity.deductHealth(damage);
        }
    }

    public void heal(float heal) {
        if ((heal + entity.getHealth()) > 100) {
            entity.setHealth(100);
        } else {
            entity.addHealth(heal);
        }
    }

    public void attack() {
        //TODO ATTACK TO PLAYER
    }

    public void die(DeathReason death) {
        boolean hasOwner = main.getPlayersManager().hasAnyPlayerEntity(id);
        int ownerId = -1;
        if(hasOwner){ 
            ownerId = main.getPlayersManager().getOwnerOfEntity(id);
            main.server.getConnection(ownerId).send(new OpenGuiMessage(Utils.getRespawnGui(10)));
        }
        entity.setHealth(100);
        serverDataStorage.respawnEntity(id);
        System.out.println("[INFO] Entity " + id + " died (" + death.toString() + ")");
        if(hasOwner){
            main.server.getConnection(ownerId).send(new CloseGuiMessage(100));
        }
    }

    @Override
    public void update(float tpf) {
        if (entity.isSpawned()) {

            if (entity.getLocation().getY() < -200f) {
                die(DeathReason.FALL_INTO_VOID);
                return;
            }

            if (serverDataStorage.getControl(id).onGround()) {
                if ((lastOnGroundY > entity.getLocation().getY())
                        && (highestAirY > entity.getLocation().getY())) {
                    if ((lastOnGroundY - entity.getLocation().getY()) > (highestAirY - entity.getLocation().getY())) {
                        float y = lastOnGroundY - entity.getLocation().getY();
                        if (y > height_without_damage) {
                            float d = y * damage_for_one_float;
                            damage(d);
                            if (entity.getHealth() <= 0f) {
                                lastOnGroundY = 0f;
                                highestAirY = 0f;
                                die(DeathReason.FALL_FROM_HIGH_PLACE);
                                return;
                            }
                        }
                    } else if (highestAirY > entity.getLocation().getY()) {
                        float y = highestAirY - entity.getLocation().getY();
                        if (y > height_without_damage) {
                            float d = y * damage_for_one_float;
                            damage(d);
                            if (entity.getHealth() <= 0f) {
                                if ((highestAirY - entity.getLocation().getY()) > 100f) {
                                    lastOnGroundY = 0f;
                                    highestAirY = 0f;
                                    die(DeathReason.FALL_FROM_THE_SKY);
                                    return;
                                } else {
                                    lastOnGroundY = 0f;
                                    highestAirY = 0f;
                                    die(DeathReason.FALL_FROM_HIGH_HEIGHT);
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    if ((lastOnGroundY > entity.getLocation().getY())) {
                        float y = lastOnGroundY - entity.getLocation().getY();
                        if (y > height_without_damage) {
                            float d = y * damage_for_one_float;
                            damage(d);
                            if (entity.getHealth() <= 0f) {
                                lastOnGroundY = 0f;
                                highestAirY = 0f;
                                die(DeathReason.FALL_FROM_HIGH_PLACE);
                                return;
                            }
                        }
                    } else if (highestAirY > entity.getLocation().getY()) {
                        float y = highestAirY - entity.getLocation().getY();
                        if (y > height_without_damage) {
                            float d = y * damage_for_one_float;
                            damage(d);
                            if (entity.getHealth() <= 0f) {
                                if ((highestAirY - entity.getLocation().getY()) > 100f) {
                                    lastOnGroundY = 0f;
                                    highestAirY = 0f;
                                    die(DeathReason.FALL_FROM_THE_SKY);
                                    return;
                                } else {
                                    lastOnGroundY = 0f;
                                    highestAirY = 0f;
                                    die(DeathReason.FALL_FROM_HIGH_HEIGHT);
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            if (entity.getHealth() <= 0f) {
                if (attacked = true) {
                    die(DeathReason.KILLED_BY_PLAYER);
                } else {
                    die(DeathReason.UNKNOWN);
                }
                return;
            }

            if (attacked == true) {
                attackedtime = attackedtime + tpf;
                if (attackedtime == maxAttackedtime) {
                    attacked = false;
                    attackedtime = 0f;
                }
            }

            if (attacked == false && attackedtime != 0f) {
                attackedtime = 0f;
            }

            if (jump) {
                serverDataStorage.getControl(id).jump();
            }

            camDir.y = 0f;
            camLeft.y = 0f;
            camDir.normalizeLocal();
            camLeft.normalizeLocal();
            Vector3f walkDirection = new Vector3f(0, 0, 0);

            if (left) {
                walkDirection.addLocal(camLeft);
            }
            if (right) {
                walkDirection.addLocal(camLeft.negate());
            }
            if (up) {
                walkDirection.addLocal(camDir);
            }
            if (down) {
                walkDirection.addLocal(camDir.negate());
            }

            if (serverDataStorage.containsAirTime(id) == false) {
                serverDataStorage.setAirTime(id, 0f);
            }
            if (!serverDataStorage.getControl(id).onGround()) {
                serverDataStorage.setAirTime(id, serverDataStorage.getAirTime(id) + tpf);
                if (highestAirY < entity.getLocation().getY()) {
                    highestAirY = entity.getLocation().getY();
                }
            } else {
                serverDataStorage.setAirTime(id, 0f);
                lastOnGroundY = entity.getLocation().getY();
                highestAirY = 0f;
            }
            if (walkDirection.lengthSquared() == 0) {
                entity.setActualAnim("Stand");
            } else {
                serverDataStorage.getControl(id).setViewDirection(walkDirection);
                if (serverDataStorage.getAirTime(id) > .3f) {
                    entity.setActualAnim("Stand");
                } else {
                    entity.setActualAnim("Walk");
                }
            }

            walkDirection.multLocal(5f).multLocal(0.05f);
            serverDataStorage.getControl(id).setWalkDirection(walkDirection);
        }
    }

    public Vector3f getCamDir() {
        return camDir;
    }

    public void setCamDir(Vector3f camDir) {
        this.camDir = camDir;
    }

    public Vector3f getCamLeft() {
        return camLeft;
    }

    public void setCamLeft(Vector3f camLeft) {
        this.camLeft = camLeft;
    }

}
